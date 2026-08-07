package com.mps.users.application;

import java.util.EnumSet;
import java.util.Set;

import com.mps.acessos.domain.TipoAcesso;
import com.mps.shared.logging.AppLoggerFactory;
import com.mps.shared.logging.Logger;
import com.mps.shared.observer.AcessoObserver;
import com.mps.shared.observer.EventoDeAcesso;

public class NotificacaoVendedorObserver implements AcessoObserver {

    private static final Set<TipoAcesso> ACOES_NOTIFICAVEIS =
            EnumSet.of(TipoAcesso.REMOCAO, TipoAcesso.REATIVACAO);

    private final Logger logger;

    public NotificacaoVendedorObserver() {
        this(AppLoggerFactory.getLogger(NotificacaoVendedorObserver.class));
    }

    NotificacaoVendedorObserver(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void aoOcorrer(EventoDeAcesso evento) {
        if (ACOES_NOTIFICAVEIS.contains(evento.acao())) {
            logger.warn(mensagemPara(evento));
        }
    }

    private String mensagemPara(EventoDeAcesso evento) {
        return evento.acao() == TipoAcesso.REMOCAO
                ? "Vendedor " + evento.usuarioId() + " removido; seus anúncios serão desativados"
                : "Vendedor " + evento.usuarioId() + " reativado; revise seus anúncios";
    }
}
