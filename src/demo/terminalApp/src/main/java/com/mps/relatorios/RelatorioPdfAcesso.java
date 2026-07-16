package com.mps.relatorios;

import java.io.ByteArrayOutputStream;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import com.mps.relatorios.EstatisticasAcesso.RankingUsuario;

public class RelatorioPdfAcesso extends RelatorioAcessoTemplate<RelatorioPdfAcesso.PdfContexto> {

    static final class PdfContexto {
        final ByteArrayOutputStream saida = new ByteArrayOutputStream();
        final Document documento = new Document();
    }

    @Override
    public String extensaoArquivo() {
        return ".pdf";
    }

    @Override
    protected PdfContexto abrirDocumento() {
        PdfContexto contexto = new PdfContexto();
        try {
            PdfWriter.getInstance(contexto.documento, contexto.saida);
            contexto.documento.open();
            contexto.documento.add(new Paragraph("Relatório de Acesso de Usuários"));
        } catch (DocumentException e) {
            throw new RelatorioException("Erro ao iniciar relatório PDF", e);
        }
        return contexto;
    }

    @Override
    protected void escreverCabecalho(PdfContexto contexto, EstatisticasAcesso estatisticas) {
        try {
            contexto.documento.add(new Paragraph("Total de acessos: " + estatisticas.totalAcessos()));
            contexto.documento.add(new Paragraph(
                    "Primeiro acesso: " + estatisticas.primeiroAcesso().map(Object::toString).orElse("-")));
            contexto.documento.add(new Paragraph(
                    "Último acesso: " + estatisticas.ultimoAcesso().map(Object::toString).orElse("-")));
        } catch (DocumentException e) {
            throw new RelatorioException("Erro ao escrever cabeçalho do relatório PDF", e);
        }
    }

    @Override
    protected void escreverRankingUsuarios(PdfContexto contexto, EstatisticasAcesso estatisticas) {
        try {
            contexto.documento.add(new Paragraph("Usuários mais ativos"));
            PdfPTable tabela = new PdfPTable(2);
            tabela.addCell(new PdfPCell(new Paragraph("Login")));
            tabela.addCell(new PdfPCell(new Paragraph("Acessos")));
            for (RankingUsuario item : estatisticas.ranking()) {
                tabela.addCell(item.login());
                tabela.addCell(String.valueOf(item.quantidade()));
            }
            contexto.documento.add(tabela);
        } catch (DocumentException e) {
            throw new RelatorioException("Erro ao escrever ranking do relatório PDF", e);
        }
    }

    @Override
    protected void escreverContagemPorAcao(PdfContexto contexto, EstatisticasAcesso estatisticas) {
        try {
            contexto.documento.add(new Paragraph("Acessos por tipo de ação"));
            org.openpdf.text.List lista = new org.openpdf.text.List(false, 10);
            estatisticas.porAcao().forEach((acao, quantidade) ->
                    lista.add(new org.openpdf.text.ListItem(acao + ": " + quantidade)));
            contexto.documento.add(lista);
        } catch (DocumentException e) {
            throw new RelatorioException("Erro ao escrever contagem por ação do relatório PDF", e);
        }
    }

    @Override
    protected byte[] fecharDocumento(PdfContexto contexto) {
        contexto.documento.close();
        return contexto.saida.toByteArray();
    }
}
