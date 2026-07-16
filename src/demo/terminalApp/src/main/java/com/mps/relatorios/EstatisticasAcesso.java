package com.mps.relatorios;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mps.acessos.domain.TipoAcesso;

public record EstatisticasAcesso(
        long totalAcessos,
        Map<TipoAcesso, Long> porAcao,
        List<RankingUsuario> ranking,
        Optional<Instant> primeiroAcesso,
        Optional<Instant> ultimoAcesso) {

    public record RankingUsuario(String login, long quantidade) {
    }
}
