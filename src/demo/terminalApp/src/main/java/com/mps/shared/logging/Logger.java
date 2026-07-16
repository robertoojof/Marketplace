package com.mps.shared.logging;

public interface Logger {
    void info(String mensagem);
    void warn(String mensagem);
    void error(String mensagem, Throwable causa);
}
