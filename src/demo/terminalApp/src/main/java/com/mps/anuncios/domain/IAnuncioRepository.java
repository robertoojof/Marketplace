package com.mps.anuncios.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAnuncioRepository {
    void salvar(Anuncio anuncio);
    List<Anuncio> buscarTodos();
    Optional<Anuncio> buscarPorId(UUID id);
    List<Anuncio> buscarPorVendedor(UUID vendedorId);
    Anuncio atualizar(Anuncio anuncio);
    void deletar(UUID id);
    void reativar(UUID id);
    void desativarTodosDoVendedor(UUID vendedorId);
}
