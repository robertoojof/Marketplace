package com.mps.shared.logging;

public class Slf4jLoggerAdapter implements Logger {

    private final org.slf4j.Logger slf4jLogger;

    public Slf4jLoggerAdapter(org.slf4j.Logger slf4jLogger) {
        this.slf4jLogger = slf4jLogger;
    }

    @Override
    public void info(String mensagem) {
        slf4jLogger.info(mensagem);
    }

    @Override
    public void warn(String mensagem) {
        slf4jLogger.warn(mensagem);
    }

    @Override
    public void error(String mensagem, Throwable causa) {
        slf4jLogger.error(mensagem, causa);
    }
}
