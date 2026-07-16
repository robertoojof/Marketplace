package com.mps.produtos.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProdutoRepository {
    void salvar(Produto produto);
    List<Produto> buscarTodos();
    Optional<Produto> buscarPorId(UUID id);
    Produto atualizar(Produto produto);
    void deletar(UUID id);
    void reativar(UUID id);
}
