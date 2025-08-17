package com.syakeapps.tcr.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syakeapps.tcr.application.Constraints;
import com.syakeapps.tcr.client.request.DeepSeekChatCompletionsMessagePayload;
import com.syakeapps.tcr.client.request.DeepSeekChatCompletionsPayload;
import com.syakeapps.tcr.util.JsonUtils;
import com.syakeapps.tcr.util.UriBuilder;

public class DeepSeekChatService {
    private static final Logger LOG = LoggerFactory.getLogger(DeepSeekChatService.class);
    private static List<DeepSeekChatCompletionsMessagePayload> messages;
    private static boolean isSetuped = false;

    private ConfigService conf = new ConfigService();

    public void setup(String prompt) {
        try {
            if (isSetuped) {
                LOG.debug("DeepSeek is already setuped.");
            } else {
                LOG.debug("Setting up DeepSeek: {}", prompt);

                HttpResponse<String> response = request("system", conf.get(Constraints.DEEPSEEK_SETUP_PROMPT));

                if (response.statusCode() == 200) {
                    String role = JsonUtils.getValueByKey(response.body(), "choices[0].message.role");
                    String content = JsonUtils.getValueByKey(response.body(), "choices[0].message.content");
                    messages.add(new DeepSeekChatCompletionsMessagePayload(role, content, null));
                } else {
                    throw new RuntimeException("Failed to setup DeepSeek.");
                }

                LOG.debug("Setup DeepSeek result. {} : {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            LOG.error("Failed to setup DeepSeek.", e);
        }
    }

    public String sendChat(String name, String message) {
        String result = "";

        try {
            LOG.debug("Sending chat message to DeepSeek: {}", message);

            HttpResponse<String> response = request("user", message, name);
            result = JsonUtils.getValueByKey(response.body(), "choices[0].message.content");

            if (response.statusCode() == 200) {
                String role = JsonUtils.getValueByKey(response.body(), "choices[0].message.role");
                String content = JsonUtils.getValueByKey(response.body(), "choices[0].message.content");
                messages.add(new DeepSeekChatCompletionsMessagePayload(role, content, null));
            } else {
                throw new RuntimeException("Failed to chat DeepSeek.");
            }

            LOG.debug("Chat DeepSeek result. {} : {}", response.statusCode(), response.body());
        } catch (Exception e) {
            LOG.error("Failed to chat DeepSeek.", result, e);
        }

        return result;
    }

    private HttpResponse<String> request(String role, String message) throws Exception {
        return request(role, message, null);
    }

    private HttpResponse<String> request(String role, String message, String name) throws Exception {
        URI uri = UriBuilder.create(conf.get(Constraints.DEEPSEEK_API_ENDPOINT))
                .build();

        DeepSeekChatCompletionsMessagePayload msgPayload = new DeepSeekChatCompletionsMessagePayload(role, message,
                name);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(msgPayload);

        String payload = DeepSeekChatCompletionsPayload.create()
                .model(conf.get(Constraints.DEEPSEEK_MODEL))
                .stream(false)
                .message(messages)
                .maxToken(1024)
                .build();

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + conf.get(Constraints.DEEPSEEK_API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}