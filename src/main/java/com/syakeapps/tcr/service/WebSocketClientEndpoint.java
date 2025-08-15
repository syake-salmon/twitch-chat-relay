package com.syakeapps.tcr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syakeapps.tcr.util.JsonUtils;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint
public class WebSocketClientEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientEndpoint.class);

    private WebSocketSessionStateHolder sessionState = new WebSocketSessionStateHolder();

    @OnOpen
    public void onOpen(Session session) {
        LOG.debug("[Session established]");
    }

    @OnMessage
    public void onMessage(String message) {
        LOG.debug("[Received]:{}", message);

        String msgType = JsonUtils.getValueByKey(message, "metadata.message_type");
        if (msgType.equals("session_welcome")) {
            String sessionId = JsonUtils.getValueByKey(message, "payload.session.id");

            if (sessionId != null) {
                LOG.debug("[Session ID received]: {}", sessionId);
                sessionState.setSessionId(sessionId);
            } else {
                LOG.warn("[Session ID not found in message]");
            }
        }
    }

    @OnError
    public void onError(Throwable th) {
        LOG.error("[Error]", th);
    }

    @OnClose
    public void onClose(Session session) {
        LOG.info("[Session closed]");
    }
}
