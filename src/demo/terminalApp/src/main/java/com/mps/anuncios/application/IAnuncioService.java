package com.mps.anuncios.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.domain.Anuncio;

public interface IAnuncioService {

    void adicionarAnuncio(Anuncio anuncio);

    List<Anuncio> listarAnuncios();

    Optional<Anuncio> buscarAnuncioPorId(UUID id);

    List<Anuncio> buscarAnunciosPorVendedor(UUID vendedorId);

    Anuncio atualizarAnuncio(Anuncio anuncio);

    void removerAnuncio(UUID id);

    void reativarAnuncio(UUID id);

    void desativarAnunciosDoVendedor(UUID vendedorId);
}
