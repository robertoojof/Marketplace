package com.mps.shared.command;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.mps.shared.logging.AppLoggerFactory;
import com.mps.shared.logging.Logger;

public class CommandInvoker {

    static final int LIMITE_HISTORICO = 50;

    private final Logger logger = AppLoggerFactory.getLogger(CommandInvoker.class);
    private final Deque<RegistroComando> historico = new ArrayDeque<>();

    public <T> T executar(Command<T> comando) {
        try {
            T resultado = comando.executar();
            registrar(comando.descricao());
            return resultado;
        } catch (RuntimeException e) {
            logger.error("Falha ao executar comando: " + comando.descricao(), e);
            throw e;
        }
    }

    public List<RegistroComando> getHistorico() {
        return List.copyOf(historico);
    }

    public void limparHistorico() {
        historico.clear();
    }

    private void registrar(String descricao) {
        if (historico.size() >= LIMITE_HISTORICO) {
            historico.removeFirst();
        }
        historico.addLast(new RegistroComando(Instant.now(), descricao));
        logger.info("Comando executado: " + descricao);
    }
}
