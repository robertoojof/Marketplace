package com.mps.shared.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class CommandInvokerTest {

    private static Command<String> comandoQueRetorna(String valor) {
        return new Command<>() {
            @Override
            public String executar() {
                return valor;
            }

            @Override
            public String descricao() {
                return "Comando " + valor;
            }
        };
    }

    @Test
    void executar_deve_devolver_o_resultado_do_comando() {
        CommandInvoker invoker = new CommandInvoker();

        assertEquals("ok", invoker.executar(comandoQueRetorna("ok")));
    }

    @Test
    void executar_deve_registrar_os_comandos_no_historico_em_ordem() {
        CommandInvoker invoker = new CommandInvoker();

        invoker.executar(comandoQueRetorna("A"));
        invoker.executar(comandoQueRetorna("B"));

        List<RegistroComando> historico = invoker.getHistorico();
        assertEquals(2, historico.size());
        assertEquals("Comando A", historico.get(0).descricao());
        assertEquals("Comando B", historico.get(1).descricao());
    }

    @Test
    void comando_que_falha_nao_deve_entrar_no_historico_e_deve_propagar_a_excecao() {
        CommandInvoker invoker = new CommandInvoker();
        Command<Void> falho = new Command<>() {
            @Override
            public Void executar() {
                throw new IllegalStateException("boom");
            }

            @Override
            public String descricao() {
                return "Comando falho";
            }
        };

        assertThrows(IllegalStateException.class, () -> invoker.executar(falho));
        assertTrue(invoker.getHistorico().isEmpty());
    }

    @Test
    void historico_deve_descartar_os_registros_mais_antigos_ao_atingir_o_limite() {
        CommandInvoker invoker = new CommandInvoker();

        for (int i = 0; i < CommandInvoker.LIMITE_HISTORICO + 5; i++) {
            invoker.executar(comandoQueRetorna(String.valueOf(i)));
        }

        List<RegistroComando> historico = invoker.getHistorico();
        assertEquals(CommandInvoker.LIMITE_HISTORICO, historico.size());
        assertEquals("Comando 5", historico.get(0).descricao());
    }

    @Test
    void getHistorico_deve_devolver_copia_imutavel() {
        CommandInvoker invoker = new CommandInvoker();
        invoker.executar(comandoQueRetorna("A"));

        List<RegistroComando> historico = invoker.getHistorico();

        assertThrows(UnsupportedOperationException.class, () -> historico.clear());
    }
}
