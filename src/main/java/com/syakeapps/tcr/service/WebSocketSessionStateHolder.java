package com.syakeapps.tcr.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.websocket.Session;

public class WebSocketSessionStateHolder {
    private static final AtomicReference<String> SESSION_ID_REFERENCE = new AtomicReference<>();
    private static CountDownLatch latch = new CountDownLatch(1);

    private static Session session;

    public void setSessionId(String sessionId) {
        SESSION_ID_REFERENCE.set(sessionId);
        latch.countDown();
    }

    public String getSessionId() {
        return SESSION_ID_REFERENCE.get();
    }

    public String awaitSessionId() throws InterruptedException {
        return awaitSessionId(10, TimeUnit.SECONDS);
    }

    public String awaitSessionId(long timeout, TimeUnit unit) throws InterruptedException {
        latch.await(timeout, unit);
        return getSessionId();
    }

    public void setSession(Session session) {
        WebSocketSessionStateHolder.session = session;
    }

    public Session getSession() {
        return session;
    }

    public boolean isSessionOpen() {
        return session != null && session.isOpen();
    }
}
