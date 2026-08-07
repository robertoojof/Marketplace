package com.mps.produtos.application.command;

import com.mps.produtos.domain.Produto;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.shared.command.Command;

public class AtualizarProdutoCommand implements Command<Produto> {

    private final ProdutoController receptor;
    private final Produto produto;

    public AtualizarProdutoCommand(ProdutoController receptor, Produto produto) {
        this.receptor = receptor;
        this.produto = produto;
    }

    @Override
    public Produto executar() {
        return receptor.atualizarProduto(produto);
    }

    @Override
    public String descricao() {
        return "Atualizar produto " + produto.getId();
    }
}
