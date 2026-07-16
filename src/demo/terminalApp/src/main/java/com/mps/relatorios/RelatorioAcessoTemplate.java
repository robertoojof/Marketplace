package com.mps.relatorios;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.TipoAcesso;
import com.mps.relatorios.EstatisticasAcesso.RankingUsuario;
import com.mps.users.domain.User;

public abstract class RelatorioAcessoTemplate<C> {

    public final byte[] gerar(List<AcessoLog> logs, List<User> usuarios) {
        EstatisticasAcesso estatisticas = calcularEstatisticas(logs, usuarios);
        C contexto = abrirDocumento();
        escreverCabecalho(contexto, estatisticas);
        escreverRankingUsuarios(contexto, estatisticas);
        escreverContagemPorAcao(contexto, estatisticas);
        return fecharDocumento(contexto);
    }

    public abstract String extensaoArquivo();

    protected abstract C abrirDocumento();
    protected abstract void escreverCabecalho(C contexto, EstatisticasAcesso estatisticas);
    protected abstract void escreverRankingUsuarios(C contexto, EstatisticasAcesso estatisticas);
    protected abstract void escreverContagemPorAcao(C contexto, EstatisticasAcesso estatisticas);
    protected abstract byte[] fecharDocumento(C contexto);

    private EstatisticasAcesso calcularEstatisticas(List<AcessoLog> logs, List<User> usuarios) {
        Map<UUID, String> loginPorId = usuarios.stream()
                .collect(Collectors.toMap(User::getId, User::getLogin, (a, b) -> a));

        Map<TipoAcesso, Long> porAcao = logs.stream()
                .collect(Collectors.groupingBy(AcessoLog::getAcao, Collectors.counting()));

        List<RankingUsuario> ranking = logs.stream()
                .collect(Collectors.groupingBy(AcessoLog::getUsuarioId, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new RankingUsuario(
                        loginPorId.getOrDefault(entry.getKey(), entry.getKey().toString()),
                        entry.getValue()))
                .sorted(Comparator.comparingLong(RankingUsuario::quantidade).reversed())
                .toList();

        Optional<Instant> primeiroAcesso = logs.stream().map(AcessoLog::getTimestamp).min(Instant::compareTo);
        Optional<Instant> ultimoAcesso = logs.stream().map(AcessoLog::getTimestamp).max(Instant::compareTo);

        return new EstatisticasAcesso(logs.size(), porAcao, ranking, primeiroAcesso, ultimoAcesso);
    }
}
