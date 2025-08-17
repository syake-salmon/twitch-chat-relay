package com.syakeapps.tcr.client.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeepSeekChatCompletionsPayload {

    @JsonProperty("messages")
    private List<DeepSeekChatCompletionsMessagePayload> messages;

    @JsonProperty("model")
    private String model;

    @JsonProperty("frequency_penalty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer frequencyPenalty;

    @JsonProperty("max_tokens")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer maxTokens;

    @JsonProperty("presence_penalty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer presencePenalty;

    @JsonProperty("response_format")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> responseFormat;

    @JsonProperty("stop")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> stop;

    @JsonProperty("stream")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean stream;

    @JsonProperty("stream_options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> streamOptions;

    @JsonProperty("temperature")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer temperature;

    @JsonProperty("top_p")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer topP;

    @JsonProperty("tools")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, Object>> tools;

    @JsonProperty("tool_choice")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> toolChoice;

    @JsonProperty("logprobs")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean logprobs;

    @JsonProperty("top_logprobs")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer topLogprobs;

    public static DeepSeekChatCompletionsPayload create() {
        return new DeepSeekChatCompletionsPayload();
    }

    public DeepSeekChatCompletionsPayload model(String model) {
        this.model = model;
        return this;
    }

    public DeepSeekChatCompletionsPayload message(String role, String content, String name) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }

        this.messages.add(new DeepSeekChatCompletionsMessagePayload(role, content, name));

        return this;
    }

    public DeepSeekChatCompletionsPayload message(List<DeepSeekChatCompletionsMessagePayload> messages) {
        if (this.messages == null) {
            this.messages = messages;
        } else {
            this.messages.addAll(messages);
        }

        return this;
    }

    public DeepSeekChatCompletionsPayload stream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public DeepSeekChatCompletionsPayload maxToken(Integer maxToken) {
        this.maxTokens = maxToken;
        return this;
    }

    public String build() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "DeepSeekChatPayload [model=" + model + ", messages=" + messages + ", stream=" + stream + "]";
    }
}
