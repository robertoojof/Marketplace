package com.mps.users.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mps.acessos.domain.TipoAcesso;
import com.mps.shared.logging.Logger;
import com.mps.shared.observer.EventoDeAcesso;

class NotificacaoVendedorObserverTest {

    private static class LoggerEspiao implements Logger {

        private final List<String> avisos = new ArrayList<>();

        @Override
        public void info(String mensagem) {
        }

        @Override
        public void warn(String mensagem) {
            avisos.add(mensagem);
        }

        @Override
        public void error(String mensagem, Throwable causa) {
        }
    }

    private final LoggerEspiao logger = new LoggerEspiao();
    private final NotificacaoVendedorObserver observador = new NotificacaoVendedorObserver(logger);

    @Test
    void deve_notificar_quando_vendedor_e_removido() {
        UUID vendedorId = UUID.randomUUID();

        observador.aoOcorrer(EventoDeAcesso.agora(vendedorId, TipoAcesso.REMOCAO));

        assertEquals(1, logger.avisos.size());
        assertTrue(logger.avisos.get(0).contains(vendedorId.toString()));
        assertTrue(logger.avisos.get(0).contains("anúncios serão desativados"));
    }

    @Test
    void deve_notificar_quando_vendedor_e_reativado() {
        observador.aoOcorrer(EventoDeAcesso.agora(UUID.randomUUID(), TipoAcesso.REATIVACAO));

        assertEquals(1, logger.avisos.size());
        assertTrue(logger.avisos.get(0).contains("reativado"));
    }

    @Test
    void nao_deve_notificar_em_acoes_rotineiras() {
        observador.aoOcorrer(EventoDeAcesso.agora(UUID.randomUUID(), TipoAcesso.CRIACAO));
        observador.aoOcorrer(EventoDeAcesso.agora(UUID.randomUUID(), TipoAcesso.BUSCA));
        observador.aoOcorrer(EventoDeAcesso.agora(UUID.randomUUID(), TipoAcesso.ATUALIZACAO));

        assertTrue(logger.avisos.isEmpty());
    }
}
