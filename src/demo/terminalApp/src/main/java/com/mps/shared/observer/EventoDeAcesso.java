package com.mps.shared.observer;

import java.time.Instant;
import java.util.UUID;

import com.mps.acessos.domain.TipoAcesso;

public record EventoDeAcesso(UUID usuarioId, TipoAcesso acao, Instant momento) {

    public static EventoDeAcesso agora(UUID usuarioId, TipoAcesso acao) {
        return new EventoDeAcesso(usuarioId, acao, Instant.now());
    }
}
