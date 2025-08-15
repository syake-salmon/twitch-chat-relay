package com.syakeapps.tcr.application;

import static com.syakeapps.tcr.util.RunnableWithException.wrap;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syakeapps.tcr.service.ConfigService;
import com.syakeapps.tcr.service.TwitchAuthService;
import com.syakeapps.tcr.service.TwitchAuthStateHolder;
import com.syakeapps.tcr.service.TwitchEventSubService;
import com.syakeapps.tcr.service.WebSocketClientService;
import com.syakeapps.tcr.service.WebSocketSessionStateHolder;

public class Main {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    private static ConfigService configService = new ConfigService();
    private static TwitchAuthService twitchAuthService = new TwitchAuthService();
    private static WebSocketClientService webSocketClientService = new WebSocketClientService();
    private static TwitchEventSubService eventSubService = new TwitchEventSubService();

    private static TwitchAuthStateHolder authState = new TwitchAuthStateHolder();
    private static WebSocketSessionStateHolder sessionState = new WebSocketSessionStateHolder();

    /**
     * Main method to start the Twitch Chat Relay application.
     *
     * @param args command line arguments
     */
    public static void main(String... args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            webSocketClientService.stop();
            LOG.info("Twitch Chat Relay application has stopped.");
        }));


        try {
            LOG.info("Starting Twitch Chat Relay application...");

            // Load configuration
            configService.configure();

            // authenticate with Twitch and connect to WebSocket asynchronously
            CompletableFuture.runAsync(wrap(() -> twitchAuthService.authenticate()));
            CompletableFuture.runAsync(wrap(() -> webSocketClientService.connect()));

            // Wait for both authentication and WebSocket connection to complete
            String token = authState.awaitToken(60, TimeUnit.SECONDS);
            String sessionId = sessionState.awaitSessionId(60, TimeUnit.SECONDS);

            // Subscribe to Twitch EventSub
            eventSubService.subscribe(sessionId, token);

            LOG.info("Twitch Chat Relay application started successfully.");
            while(true) {
                Thread.sleep(1000);
            }
        } catch (Exception | Error e) {
            LOG.error("An error occurred.", e);
            System.exit(1);
        }

        System.exit(0);
    }
}
