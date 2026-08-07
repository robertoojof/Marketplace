package com.mps.anuncios.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.mps.produtos.domain.Produto;
import com.mps.users.domain.User;

public final class AnuncioMemento {

    private final UUID id;
    private final Produto produto;
    private final User vendedor;
    private final BigDecimal preco;
    private final Integer quantidadeEmEstoque;
    private final boolean ativo;

    AnuncioMemento(UUID id, Produto produto, User vendedor, BigDecimal preco,
            Integer quantidadeEmEstoque, boolean ativo) {
        this.id = id;
        this.produto = produto;
        this.vendedor = vendedor;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.ativo = ativo;
    }

    public UUID getAnuncioId() {
        return id;
    }

    Produto getProduto() {
        return produto;
    }

    User getVendedor() {
        return vendedor;
    }

    BigDecimal getPreco() {
        return preco;
    }

    Integer getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    boolean isAtivo() {
        return ativo;
    }
}
