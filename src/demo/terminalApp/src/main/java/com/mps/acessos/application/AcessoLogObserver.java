package com.mps.acessos.application;

import java.util.UUID;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.IAcessoLogRepository;
import com.mps.shared.observer.AcessoObserver;
import com.mps.shared.observer.EventoDeAcesso;

public class AcessoLogObserver implements AcessoObserver {

    private final IAcessoLogRepository acessoLogRepository;

    public AcessoLogObserver(IAcessoLogRepository acessoLogRepository) {
        this.acessoLogRepository = acessoLogRepository;
    }

    @Override
    public void aoOcorrer(EventoDeAcesso evento) {
        acessoLogRepository.salvar(new AcessoLog(
                UUID.randomUUID(), evento.usuarioId(), evento.acao(), evento.momento()));
    }
}
