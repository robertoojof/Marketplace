package com.mps.anuncios.application.command;

import java.util.Optional;

import com.mps.anuncios.application.AnuncioCaretaker;
import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.AnuncioMemento;
import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.command.Command;

public class AtualizarAnuncioCommand implements Command<Anuncio> {

    private final AnuncioController receptor;
    private final AnuncioCaretaker caretaker;
    private final Anuncio anuncio;

    public AtualizarAnuncioCommand(AnuncioController receptor, AnuncioCaretaker caretaker, Anuncio anuncio) {
        this.receptor = receptor;
        this.caretaker = caretaker;
        this.anuncio = anuncio;
    }

    @Override
    public Anuncio executar() {
        Optional<AnuncioMemento> estadoAnterior = receptor.buscarAnuncioPorId(anuncio.getId())
                .map(Anuncio::criarMemento);

        Anuncio atualizado = receptor.atualizarAnuncio(anuncio);

        estadoAnterior.ifPresent(caretaker::guardar);
        return atualizado;
    }

    @Override
    public String descricao() {
        return "Atualizar anúncio " + anuncio.getId();
    }
}
