package com.syakeapps.tcr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface RunnableWithException {
    public Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());

    void run() throws Exception;

    public static Runnable wrap(RunnableWithException rwe) {
        return () -> {
            try {
                rwe.run();
            } catch (Exception e) {
                log.error("An error occurred while executing the runnable.", e);
            }
        };
    }
}
