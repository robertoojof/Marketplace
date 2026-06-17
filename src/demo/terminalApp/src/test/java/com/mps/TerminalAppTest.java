package com.mps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerminalAppTest {

    private final InputStream entradaOriginal = System.in;
    private final PrintStream saidaOriginal = System.out;
    private ByteArrayOutputStream saidaCapturada;

    @BeforeEach
    void configurar() {
        saidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saidaCapturada));
    }

    @AfterEach
    void restaurar() {
        System.setIn(entradaOriginal);
        System.setOut(saidaOriginal);
    }

    @Test
    void main_deve_encerrar_ao_receber_opcao_sair() {
        // "1\n" seleciona armazenamento em memória; "7\n" sai do menu principal
        System.setIn(new ByteArrayInputStream("1\n7\n".getBytes()));

        TerminalApp.main(new String[]{});

        assertTrue(saidaCapturada.toString().contains("Encerrando o programa..."));
    }
}
