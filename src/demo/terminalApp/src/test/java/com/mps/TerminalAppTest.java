package com.mps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
        System.setIn(new ByteArrayInputStream("1\n7\n".getBytes()));

        TerminalApp.main(new String[]{});

        assertTrue(saidaCapturada.toString().contains("Encerrando o programa..."));
    }

    @Test
    void main_deve_encerrar_sem_falhar_quando_a_entrada_termina_antes_de_sair() {
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));

        assertDoesNotThrow(() -> TerminalApp.main(new String[]{}));

        assertTrue(saidaCapturada.toString().contains("Entrada encerrada"));
    }

    @Test
    void main_deve_encerrar_sem_falhar_quando_a_entrada_termina_no_meio_de_um_cadastro() {
        System.setIn(new ByteArrayInputStream("1\n1\n1\nfulano\n".getBytes()));

        assertDoesNotThrow(() -> TerminalApp.main(new String[]{}));

        assertTrue(saidaCapturada.toString().contains("Entrada encerrada"));
    }
}
