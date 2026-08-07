package com.mps.produtos.application.command;

import com.mps.produtos.domain.Produto;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.shared.command.Command;

public class AdicionarProdutoCommand implements Command<Void> {

    private final ProdutoController receptor;
    private final Produto produto;

    public AdicionarProdutoCommand(ProdutoController receptor, Produto produto) {
        this.receptor = receptor;
        this.produto = produto;
    }

    @Override
    public Void executar() {
        receptor.adicionarProduto(produto);
        return null;
    }

    @Override
    public String descricao() {
        return "Adicionar produto " + produto.getId();
    }
}
