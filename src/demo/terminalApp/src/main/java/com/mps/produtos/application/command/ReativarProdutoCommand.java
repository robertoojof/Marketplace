package com.mps.produtos.application.command;

import java.util.UUID;

import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.shared.command.Command;

public class ReativarProdutoCommand implements Command<Void> {

    private final ProdutoController receptor;
    private final UUID id;

    public ReativarProdutoCommand(ProdutoController receptor, UUID id) {
        this.receptor = receptor;
        this.id = id;
    }

    @Override
    public Void executar() {
        receptor.reativarProduto(id);
        return null;
    }

    @Override
    public String descricao() {
        return "Reativar produto " + id;
    }
}
