package com.syakeapps.tcr.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syakeapps.tcr.application.Constraints;
import com.syakeapps.tcr.client.request.TwitchCreateSubscriptionPayload;
import com.syakeapps.tcr.util.UriBuilder;

/**
 * Service for managing Twitch EventSub subscriptions.
 * This service handles the subscription to Twitch EventSub for chat messages.
 */
public class TwitchEventSubService {
    private static final Logger LOG = LoggerFactory.getLogger(TwitchEventSubService.class);
    private ConfigService conf = new ConfigService();

    /**
     * Subscribes to Twitch EventSub for chat messages.
     *
     * @param sessionId the WebSocket session ID
     * @param token the OAuth token for authentication
     * @throws Exception if an error occurs during subscription
     */
    public void subscribe(String sessionId, String token) throws Exception {
        LOG.debug("Subscribing to Twitch EventSub.");

        URI uri = UriBuilder.create(conf.get(Constraints.TWITCH_EVENTSUB_ENDPOINT))
                .path("subscriptions")
                .build();

        String payload = TwitchCreateSubscriptionPayload.create()
                .type("channel.chat.message")
                .version("1")
                .broadcasterUserId(conf.get(Constraints.TARGET_BROADCASTER_ID))
                .userId(conf.get(Constraints.TARGET_USER_ID))
                .method("websocket")
                .sessionId(sessionId)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .header("Client-Id", conf.get(Constraints.TWITCH_CLIENT_ID))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 202) {
            LOG.error("Failed to subscribe to Twitch EventSub: {} - {}", response.statusCode(), response.body());
            throw new RuntimeException("Failed to subscribe to Twitch EventSub.");
        }

        LOG.debug("Successfully subscribed to Twitch EventSub.");
    }
}
