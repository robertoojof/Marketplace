package com.mps.anuncios.application.command;

import java.util.UUID;

import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.command.Command;

public class RemoverAnuncioCommand implements Command<Void> {

    private final AnuncioController receptor;
    private final UUID id;

    public RemoverAnuncioCommand(AnuncioController receptor, UUID id) {
        this.receptor = receptor;
        this.id = id;
    }

    @Override
    public Void executar() {
        receptor.removerAnuncio(id);
        return null;
    }

    @Override
    public String descricao() {
        return "Remover anúncio " + id;
    }
}
