package com.mps.shared.logging;

import org.slf4j.LoggerFactory;

public final class AppLoggerFactory {

    private AppLoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Slf4jLoggerAdapter(LoggerFactory.getLogger(clazz));
    }
}
