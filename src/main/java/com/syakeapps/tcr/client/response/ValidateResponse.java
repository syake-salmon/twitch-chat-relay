package com.syakeapps.tcr.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateResponse {
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("login")
    private String login;

    @JsonProperty("scopes")
    private String[] scopes;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("expires_in")
    private int expiresIn;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "ValidateResponse{" +
                "clientId='" + clientId + '\'' +
                ", login='" + login + '\'' +
                ", scopes=" + java.util.Arrays.toString(scopes) +
                ", userId='" + userId + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
