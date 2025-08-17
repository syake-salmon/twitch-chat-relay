package com.syakeapps.tcr.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeepSeekChatCompletionsMessagePayload {
    @JsonProperty("role")
    private String role;

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonProperty("content")
    private String content;

    public DeepSeekChatCompletionsMessagePayload(String role, String content, String name) {
        this.role = role;
        this.content = content;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message [role=" + role + ", content=" + content + "]";
    }
}
