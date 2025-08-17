package com.syakeapps.tcr.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TwitchCreateSubscriptionPayload {
    @JsonProperty("type")
    String type;

    @JsonProperty("version")
    String version;

    @JsonProperty("condition")
    Condition condition;

    @JsonProperty("transport")
    Transport transport;

    public static class Condition {
        @JsonProperty("broadcaster_user_id")
        String broadcasterUserId;

        @JsonProperty("user_id")
        String userId;

        public String getBroadcasterUserId() {
            return broadcasterUserId;
        }

        public void setBroadcasterUserId(String broadcasterUserId) {
            this.broadcasterUserId = broadcasterUserId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "Condition{" +
                    "broadcasterUserId='" + broadcasterUserId + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }

    public static class Transport {
        @JsonProperty("method")
        String method;

        @JsonProperty("session_id")
        String sessionId;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        @Override
        public String toString() {
            return "Transport{" +
                    "method='" + method + '\'' +
                    ", sessionId='" + sessionId + '\'' +
                    '}';
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public static TwitchCreateSubscriptionPayload create() {
        return new TwitchCreateSubscriptionPayload();
    }

    public TwitchCreateSubscriptionPayload type(String type) {
        this.type = type;
        return this;
    }

    public TwitchCreateSubscriptionPayload version(String version) {
        this.version = version;
        return this;
    }

    public TwitchCreateSubscriptionPayload broadcasterUserId(String broadcasterUserId) {
        if (this.condition == null) {
            this.condition = new Condition();
        }

        this.condition.broadcasterUserId = broadcasterUserId;
        return this;
    }

    public TwitchCreateSubscriptionPayload userId(String userId) {
        if (this.condition == null) {
            this.condition = new Condition();
        }

        this.condition.userId = userId;
        return this;
    }

    public TwitchCreateSubscriptionPayload method(String method) {
        if (this.transport == null) {
            this.transport = new Transport();
        }

        this.transport.method = method;
        return this;
    }

    public TwitchCreateSubscriptionPayload sessionId(String sessionId) {
        if (this.transport == null) {
            this.transport = new Transport();
        }

        this.transport.sessionId = sessionId;
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
        return "TwicthCreateSubscriptionPayload{" +
                "type='" + type + '\'' +
                ", version='" + version + '\'' +
                ", condition=" + condition +
                ", transport=" + transport +
                '}';
    }
}
