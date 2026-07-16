package com.mps.anuncios.presentation.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.application.AnuncioService;
import com.mps.anuncios.domain.Anuncio;

public class AnuncioController {

    private final AnuncioService anuncioService;

    public AnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    public void adicionarAnuncio(Anuncio anuncio) {
        anuncioService.adicionarAnuncio(anuncio);
    }

    public List<Anuncio> listarAnuncios() {
        return anuncioService.listarAnuncios();
    }

    public Optional<Anuncio> buscarAnuncioPorId(UUID id) {
        return anuncioService.buscarAnuncioPorId(id);
    }

    public List<Anuncio> buscarAnunciosPorVendedor(UUID vendedorId) {
        return anuncioService.buscarAnunciosPorVendedor(vendedorId);
    }

    public Anuncio atualizarAnuncio(Anuncio anuncio) {
        return anuncioService.atualizarAnuncio(anuncio);
    }

    public void removerAnuncio(UUID id) {
        anuncioService.removerAnuncio(id);
    }

    public void reativarAnuncio(UUID id) {
        anuncioService.reativarAnuncio(id);
    }

    public void desativarAnunciosDoVendedor(UUID vendedorId) {
        anuncioService.desativarAnunciosDoVendedor(vendedorId);
    }
}
