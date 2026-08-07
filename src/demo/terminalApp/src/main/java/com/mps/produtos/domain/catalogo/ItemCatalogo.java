package com.mps.produtos.domain.catalogo;

import com.mps.produtos.domain.Produto;

public class ItemCatalogo implements ComponenteCatalogo {

    private final Produto produto;

    public ItemCatalogo(Produto produto) {
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    @Override
    public String getNome() {
        return produto.getNome();
    }

    @Override
    public int contarProdutos() {
        return 1;
    }

    @Override
    public String exibir(int nivel) {
        return Recuo.ate(nivel) + "- " + produto.getNome() + situacao() + System.lineSeparator();
    }

    private String situacao() {
        return produto.isAtivo() ? "" : " (inativo)";
    }
}
