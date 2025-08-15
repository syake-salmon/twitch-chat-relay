package com.syakeapps.tcr.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TwitchAuthStateHolder {
    private static final AtomicReference<String> TOKEN_REFERENCE = new AtomicReference<>();
    private static CountDownLatch latch = new CountDownLatch(1);

    public void setToken(String token) {
        TOKEN_REFERENCE.set(token);
        latch.countDown();
    }

    public String getToken() {
        return TOKEN_REFERENCE.get();
    }

    public String awaitToken() throws InterruptedException {
        return awaitToken(10, TimeUnit.SECONDS);
    }

    public String awaitToken(long timeout, TimeUnit unit) throws InterruptedException {
        latch.await(timeout, unit);
        return getToken();
    }
}
