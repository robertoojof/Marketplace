package com.mps.produtos.domain.catalogo;

import java.util.List;

public interface ComponenteCatalogo {

    String getNome();

    int contarProdutos();

    String exibir(int nivel);

    default List<ComponenteCatalogo> getFilhos() {
        return List.of();
    }

    default void adicionar(ComponenteCatalogo componente) {
        throw new UnsupportedOperationException("Este componente do catálogo não aceita filhos");
    }

    default void remover(ComponenteCatalogo componente) {
        throw new UnsupportedOperationException("Este componente do catálogo não possui filhos");
    }
}
