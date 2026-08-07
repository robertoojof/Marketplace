package com.mps.anuncios.application.command;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.command.Command;

public class AdicionarAnuncioCommand implements Command<Void> {

    private final AnuncioController receptor;
    private final Anuncio anuncio;

    public AdicionarAnuncioCommand(AnuncioController receptor, Anuncio anuncio) {
        this.receptor = receptor;
        this.anuncio = anuncio;
    }

    @Override
    public Void executar() {
        receptor.adicionarAnuncio(anuncio);
        return null;
    }

    @Override
    public String descricao() {
        return "Adicionar anúncio " + anuncio.getId();
    }
}
