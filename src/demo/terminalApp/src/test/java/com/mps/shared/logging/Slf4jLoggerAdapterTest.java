package com.mps.shared.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.read.ListAppender;

class Slf4jLoggerAdapterTest {

    private ch.qos.logback.classic.Logger logbackLogger;
    private ListAppender<ILoggingEvent> appender;
    private Logger logger;

    @BeforeEach
    void configurar() {
        logbackLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(Slf4jLoggerAdapterTest.class);
        appender = new ListAppender<>();
        appender.start();
        logbackLogger.addAppender(appender);
        logbackLogger.setLevel(Level.TRACE);

        logger = new Slf4jLoggerAdapter(logbackLogger);
    }

    @AfterEach
    void limpar() {
        logbackLogger.detachAppender(appender);
    }

    @Test
    void info_deve_gerar_evento_de_nivel_info() {
        logger.info("mensagem de info");

        ILoggingEvent evento = appender.list.get(0);
        assertEquals(Level.INFO, evento.getLevel());
        assertEquals("mensagem de info", evento.getFormattedMessage());
    }

    @Test
    void warn_deve_gerar_evento_de_nivel_warn() {
        logger.warn("mensagem de warn");

        ILoggingEvent evento = appender.list.get(0);
        assertEquals(Level.WARN, evento.getLevel());
        assertEquals("mensagem de warn", evento.getFormattedMessage());
    }

    @Test
    void error_deve_gerar_evento_de_nivel_error_com_a_causa() {
        RuntimeException causa = new RuntimeException("falha original");

        logger.error("mensagem de erro", causa);

        ILoggingEvent evento = appender.list.get(0);
        assertEquals(Level.ERROR, evento.getLevel());
        assertEquals("mensagem de erro", evento.getFormattedMessage());
        assertSame(causa, ((ThrowableProxy) evento.getThrowableProxy()).getThrowable());
    }
}
