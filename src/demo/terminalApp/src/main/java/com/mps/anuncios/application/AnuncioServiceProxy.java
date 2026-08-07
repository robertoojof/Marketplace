package com.mps.anuncios.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.domain.Anuncio;
import com.mps.shared.exception.AutorizacaoException;
import com.mps.shared.security.SessaoUsuario;

public class AnuncioServiceProxy implements IAnuncioService {

    private final IAnuncioService anuncioServiceReal;
    private final SessaoUsuario sessao;

    public AnuncioServiceProxy(IAnuncioService anuncioServiceReal, SessaoUsuario sessao) {
        this.anuncioServiceReal = anuncioServiceReal;
        this.sessao = sessao;
    }

    @Override
    public List<Anuncio> listarAnuncios() {
        return anuncioServiceReal.listarAnuncios();
    }

    @Override
    public Optional<Anuncio> buscarAnuncioPorId(UUID id) {
        return anuncioServiceReal.buscarAnuncioPorId(id);
    }

    @Override
    public List<Anuncio> buscarAnunciosPorVendedor(UUID vendedorId) {
        return anuncioServiceReal.buscarAnunciosPorVendedor(vendedorId);
    }

    @Override
    public void adicionarAnuncio(Anuncio anuncio) {
        exigirAutenticacao();
        UUID vendedorId = anuncio.getVendedor() == null ? null : anuncio.getVendedor().getId();
        if (!sessao.ehAdmin() && !sessao.ehUsuario(vendedorId)) {
            throw new AutorizacaoException("Você só pode criar anúncios em seu próprio nome");
        }
        anuncioServiceReal.adicionarAnuncio(anuncio);
    }

    @Override
    public Anuncio atualizarAnuncio(Anuncio anuncio) {
        exigirPropriedadeDoAnuncio(anuncio.getId(), "alterá-lo");
        return anuncioServiceReal.atualizarAnuncio(anuncio);
    }

    @Override
    public void removerAnuncio(UUID id) {
        exigirPropriedadeDoAnuncio(id, "removê-lo");
        anuncioServiceReal.removerAnuncio(id);
    }

    @Override
    public void reativarAnuncio(UUID id) {
        exigirPropriedadeDoAnuncio(id, "reativá-lo");
        anuncioServiceReal.reativarAnuncio(id);
    }

    @Override
    public void desativarAnunciosDoVendedor(UUID vendedorId) {
        exigirAutenticacao();
        if (!sessao.ehAdmin()) {
            throw new AutorizacaoException("Apenas administradores podem desativar os anúncios de um vendedor");
        }
        anuncioServiceReal.desativarAnunciosDoVendedor(vendedorId);
    }

    private void exigirAutenticacao() {
        if (!sessao.estaAutenticado()) {
            throw new AutorizacaoException("É necessário estar autenticado para executar esta operação");
        }
    }

    private void exigirPropriedadeDoAnuncio(UUID anuncioId, String acaoNoInfinitivo) {
        exigirAutenticacao();
        if (sessao.ehAdmin()) {
            return;
        }

        UUID vendedorId = anuncioServiceReal.buscarAnuncioPorId(anuncioId)
                .map(anuncio -> anuncio.getVendedor().getId())
                .orElseThrow(() -> new AutorizacaoException("Anúncio não encontrado: " + anuncioId));

        if (!sessao.ehUsuario(vendedorId)) {
            throw new AutorizacaoException(
                    "Apenas o vendedor dono do anúncio pode " + acaoNoInfinitivo);
        }
    }
}
