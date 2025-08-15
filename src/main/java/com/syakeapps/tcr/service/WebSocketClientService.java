package com.syakeapps.tcr.service;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syakeapps.tcr.application.Constraints;
import com.syakeapps.tcr.util.UriBuilder;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

public class WebSocketClientService {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientService.class);

    private ConfigService conf = new ConfigService();
    private WebSocketSessionStateHolder sessionStateHolder = new WebSocketSessionStateHolder();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "twitch-ws-keepalive");
        t.setDaemon(true);
        return t;
    });
    private volatile ScheduledFuture<?> keepAliveTask;

    private final Duration healthCheckInterval = Duration.ofSeconds(5);
    private final Duration reconnectDelay = Duration.ofSeconds(3);

    /**
     * Connects to the Twitch WebSocket endpoint.
     * This method establishes a WebSocket connection to the Twitch chat relay
     * service.
     */
    public synchronized void connect() {
        try {
            LOG.debug("Connecting to Twitch WebSocket endpoint...");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            URI uri = UriBuilder.create(conf.get(Constraints.TWITCH_WEBSOCKET_ENDPOINT))
                    .queryParam("keepalive_timeout_seconds",
                            conf.get(Constraints.TWITCH_WEBSOCKET_TIMEOUT_SECONDS, "10"))
                    .build();

            // Connect to the WebSocket server
            Session session = container.connectToServer(new WebSocketClientEndpoint(), uri);

            // Set the session in the state holder
            sessionStateHolder.setSession(session);

            // Start the keep-alive mechanism
            startKeepAlive();

            LOG.debug("Connected to Twitch WebSocket endpoint.");
        } catch (Exception e) {
            LOG.error("Connection failed. Will retry in {} seconds", reconnectDelay.toSeconds(), e);
            scheduleReconnect();
        }
    }

    /**
     * Starts the keep-alive mechanism for the WebSocket connection.
     * This method periodically sends a ping to the WebSocket server to keep the
     * connection alive.
     */
    public synchronized void startKeepAlive() {
        // Cancel any existing keep-alive task
        if (keepAliveTask != null && !keepAliveTask.isCancelled()) {
            keepAliveTask.cancel(false);
        }

        keepAliveTask = scheduler.scheduleAtFixedRate(
                this::checkAndReconnect,
                healthCheckInterval.toSeconds(),
                healthCheckInterval.toSeconds(),
                TimeUnit.SECONDS);
        LOG.debug("Keep-alive monitoring started.");
    }

    /**
     * Stops the WebSocket client service and cancels the keep-alive task.
     * This method should be called when the application is shutting down to clean
     * up resources.
     */
    public void stop() {
        if (keepAliveTask != null) {
            keepAliveTask.cancel(false);
        }

        scheduler.shutdownNow();
        LOG.debug("WebSocketClientService stopped.");
    }

    private void checkAndReconnect() {
        Session session = sessionStateHolder.getSession();
        if (session == null || !session.isOpen()) {
            LOG.warn("Detected closed or null session. Reconnecting...");
            connect();
        } else {
            try {
                session.getAsyncRemote().sendPing(ByteBuffer.wrap(new byte[] { 'p', 'i', 'n', 'g' }));
                LOG.trace("Ping sent to keep connection alive.");
            } catch (Exception e) {
                LOG.error("Ping failed, scheduling reconnect.", e);
                scheduleReconnect();
            }
        }
    }

    private void scheduleReconnect() {
        scheduler.schedule(this::connect, reconnectDelay.toSeconds(), TimeUnit.SECONDS);
    }
}
