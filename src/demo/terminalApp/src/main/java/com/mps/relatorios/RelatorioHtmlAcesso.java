package com.mps.relatorios;

import java.nio.charset.StandardCharsets;

import com.mps.relatorios.EstatisticasAcesso.RankingUsuario;

public class RelatorioHtmlAcesso extends RelatorioAcessoTemplate<StringBuilder> {

    @Override
    public String extensaoArquivo() {
        return ".html";
    }

    @Override
    protected StringBuilder abrirDocumento() {
        return new StringBuilder("<html><head><meta charset=\"UTF-8\"><title>Relatório de Acesso</title></head><body>")
                .append("<h1>Relatório de Acesso de Usuários</h1>");
    }

    @Override
    protected void escreverCabecalho(StringBuilder contexto, EstatisticasAcesso estatisticas) {
        contexto.append("<p>Total de acessos: <strong>").append(estatisticas.totalAcessos()).append("</strong></p>")
                .append("<p>Primeiro acesso: ").append(estatisticas.primeiroAcesso().map(Object::toString).orElse("-")).append("</p>")
                .append("<p>Último acesso: ").append(estatisticas.ultimoAcesso().map(Object::toString).orElse("-")).append("</p>");
    }

    @Override
    protected void escreverRankingUsuarios(StringBuilder contexto, EstatisticasAcesso estatisticas) {
        contexto.append("<h2>Usuários mais ativos</h2><table border=\"1\"><tr><th>Login</th><th>Acessos</th></tr>");
        for (RankingUsuario item : estatisticas.ranking()) {
            contexto.append("<tr><td>").append(item.login()).append("</td><td>")
                    .append(item.quantidade()).append("</td></tr>");
        }
        contexto.append("</table>");
    }

    @Override
    protected void escreverContagemPorAcao(StringBuilder contexto, EstatisticasAcesso estatisticas) {
        contexto.append("<h2>Acessos por tipo de ação</h2><ul>");
        estatisticas.porAcao().forEach((acao, quantidade) ->
                contexto.append("<li>").append(acao).append(": ").append(quantidade).append("</li>"));
        contexto.append("</ul>");
    }

    @Override
    protected byte[] fecharDocumento(StringBuilder contexto) {
        contexto.append("</body></html>");
        return contexto.toString().getBytes(StandardCharsets.UTF_8);
    }
}
