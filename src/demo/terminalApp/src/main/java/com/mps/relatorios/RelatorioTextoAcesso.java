package com.mps.relatorios;

import java.nio.charset.StandardCharsets;

import com.mps.relatorios.EstatisticasAcesso.RankingUsuario;

public class RelatorioTextoAcesso extends RelatorioAcessoTemplate<StringBuilder> {

    @Override
    public String extensaoArquivo() {
        return ".txt";
    }

    @Override
    protected StringBuilder abrirDocumento() {
        return new StringBuilder("=== Relatório de Acesso de Usuários ===\n\n");
    }

    @Override
    protected void escreverCabecalho(StringBuilder contexto, EstatisticasAcesso estatisticas) {
        contexto.append("Total de acessos: ").append(estatisticas.totalAcessos()).append('\n');
        contexto.append("Primeiro acesso: ")
                .append(estatisticas.primeiroAcesso().map(Object::toString).orElse("-")).append('\n');
        contexto.append("Último acesso: ")
                .append(estatisticas.ultimoAcesso().map(Object::toString).orElse("-")).append("\n\n");
    }

    @Override
    protected void escreverRankingUsuarios(StringBuilder contexto, EstatisticasAcesso estatisticas) {
        contexto.append("--- Usuários mais ativos ---\n");
        for (RankingUsuario item : estatisticas.ranking()) {
            contexto.append("  ").append(item.login()).append(": ").append(item.quantidade()).append('\n');
        }
        contexto.append('\n');
    }

    @Override
    protected void escreverContagemPorAcao(StringBuilder contexto, EstatisticasAcesso estatisticas) {
        contexto.append("--- Acessos por tipo de ação ---\n");
        estatisticas.porAcao().forEach((acao, quantidade) ->
                contexto.append("  ").append(acao).append(": ").append(quantidade).append('\n'));
    }

    @Override
    protected byte[] fecharDocumento(StringBuilder contexto) {
        return contexto.toString().getBytes(StandardCharsets.UTF_8);
    }
}
