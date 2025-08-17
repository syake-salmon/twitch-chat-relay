package com.syakeapps.tcr.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class DeepSeekChatCompletionResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("created")
    private Long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    @JsonProperty("object")
    private String object;

    @JsonProperty("usage")
    private Usage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    public void setSystemFingerprint(String systemFingerprint) {
        this.systemFingerprint = systemFingerprint;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "DeepSeekChatCompletionResponse [id=" + id + ", choices=" + choices + ", created=" + created + ", model="
                + model + ", systemFingerprint=" + systemFingerprint + ", object=" + object + ", usage=" + usage + "]";
    }

    public static class Choice {
        @JsonProperty("finish_reason")
        private String finishReason;

        @JsonProperty("index")
        private int index;

        @JsonProperty("message")
        private Message message;

        @JsonProperty("logprobs")
        private Logprobs logprobs;

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public Logprobs getLogprobs() {
            return logprobs;
        }

        public void setLogprobs(Logprobs logprobs) {
            this.logprobs = logprobs;
        }

        @Override
        public String toString() {
            return "Choice [finishReason=" + finishReason + ", index=" + index + ", message=" + message + ", logprobs="
                    + logprobs + "]";
        }
    }

    public static class Message {
        @JsonProperty("content")
        private String content;

        @JsonProperty("reasoning_content")
        private String reasoningContent;

        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        @JsonProperty("role")
        private String role;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReasoningContent() {
            return reasoningContent;
        }

        public void setReasoningContent(String reasoningContent) {
            this.reasoningContent = reasoningContent;
        }

        public List<ToolCall> getToolCalls() {
            return toolCalls;
        }

        public void setToolCalls(List<ToolCall> toolCalls) {
            this.toolCalls = toolCalls;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return "Message [content=" + content + ", reasoningContent=" + reasoningContent + ", toolCalls=" + toolCalls
                    + ", role=" + role + "]";
        }
    }

    public static class ToolCall {
        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("function")
        private FunctionCall function;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public FunctionCall getFunction() {
            return function;
        }

        public void setFunction(FunctionCall function) {
            this.function = function;
        }

        @Override
        public String toString() {
            return "ToolCall [id=" + id + ", type=" + type + ", function=" + function + "]";
        }
    }

    public static class FunctionCall {
        @JsonProperty("name")
        private String name;

        @JsonProperty("arguments")
        private String arguments;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArguments() {
            return arguments;
        }

        public void setArguments(String arguments) {
            this.arguments = arguments;
        }

        @Override
        public String toString() {
            return "FunctionCall [name=" + name + ", arguments=" + arguments + "]";
        }
    }

    public static class Logprobs {
        @JsonProperty("content")
        private List<LogprobContent> content;

        public List<LogprobContent> getContent() {
            return content;
        }

        public void setContent(List<LogprobContent> content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Logprobs [content=" + content + "]";
        }
    }

    public static class LogprobContent {
        @JsonProperty("token")
        private String token;

        @JsonProperty("logprob")
        private double logprob;

        @JsonProperty("bytes")
        private List<Integer> bytes;

        @JsonProperty("top_logprobs")
        private List<TopLogprob> topLogprobs;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public double getLogprob() {
            return logprob;
        }

        public void setLogprob(double logprob) {
            this.logprob = logprob;
        }

        public List<Integer> getBytes() {
            return bytes;
        }

        public void setBytes(List<Integer> bytes) {
            this.bytes = bytes;
        }

        public List<TopLogprob> getTopLogprobs() {
            return topLogprobs;
        }

        public void setTopLogprobs(List<TopLogprob> topLogprobs) {
            this.topLogprobs = topLogprobs;
        }

        @Override
        public String toString() {
            return "LogprobContent [token=" + token + ", logprob=" + logprob + ", bytes=" + bytes + ", topLogprobs="
                    + topLogprobs + "]";
        }
    }

    public static class TopLogprob {
        @JsonProperty("token")
        private String token;

        @JsonProperty("logprob")
        private double logprob;

        @JsonProperty("bytes")
        private List<Integer> bytes;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public double getLogprob() {
            return logprob;
        }

        public void setLogprob(double logprob) {
            this.logprob = logprob;
        }

        public List<Integer> getBytes() {
            return bytes;
        }

        public void setBytes(List<Integer> bytes) {
            this.bytes = bytes;
        }

        @Override
        public String toString() {
            return "TopLogprob [token=" + token + ", logprob=" + logprob + ", bytes=" + bytes + "]";
        }
    }

    public static class Usage {
        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("prompt_cache_hit_tokens")
        private int promptCacheHitTokens;

        @JsonProperty("prompt_cache_miss_tokens")
        private int promptCacheMissTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;

        @JsonProperty("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;

        public int getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }

        public int getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }

        public int getPromptCacheHitTokens() {
            return promptCacheHitTokens;
        }

        public void setPromptCacheHitTokens(int promptCacheHitTokens) {
            this.promptCacheHitTokens = promptCacheHitTokens;
        }

        public int getPromptCacheMissTokens() {
            return promptCacheMissTokens;
        }

        public void setPromptCacheMissTokens(int promptCacheMissTokens) {
            this.promptCacheMissTokens = promptCacheMissTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }

        public CompletionTokensDetails getCompletionTokensDetails() {
            return completionTokensDetails;
        }

        public void setCompletionTokensDetails(CompletionTokensDetails completionTokensDetails) {
            this.completionTokensDetails = completionTokensDetails;
        }

        @Override
        public String toString() {
            return "Usage [completionTokens=" + completionTokens + ", promptTokens=" + promptTokens
                    + ", promptCacheHitTokens=" + promptCacheHitTokens + ", promptCacheMissTokens="
                    + promptCacheMissTokens
                    + ", totalTokens=" + totalTokens + ", completionTokensDetails=" + completionTokensDetails + "]";
        }
    }

    public static class CompletionTokensDetails {
        @JsonProperty("reasoning_tokens")
        private int reasoningTokens;

        public int getReasoningTokens() {
            return reasoningTokens;
        }

        public void setReasoningTokens(int reasoningTokens) {
            this.reasoningTokens = reasoningTokens;
        }

        @Override
        public String toString() {
            return "CompletionTokensDetails [reasoningTokens=" + reasoningTokens + "]";
        }
    }
}