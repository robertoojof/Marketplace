package com.mps.anuncios.application.command;

import java.util.UUID;

import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.command.Command;

public class DesativarAnunciosDoVendedorCommand implements Command<Void> {

    private final AnuncioController receptor;
    private final UUID vendedorId;

    public DesativarAnunciosDoVendedorCommand(AnuncioController receptor, UUID vendedorId) {
        this.receptor = receptor;
        this.vendedorId = vendedorId;
    }

    @Override
    public Void executar() {
        receptor.desativarAnunciosDoVendedor(vendedorId);
        return null;
    }

    @Override
    public String descricao() {
        return "Desativar anúncios do vendedor " + vendedorId;
    }
}
