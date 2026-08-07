package com.mps.anuncios.application.command;

import com.mps.anuncios.application.AnuncioCaretaker;
import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.AnuncioMemento;
import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.command.Command;
import com.mps.shared.exception.DesfazerException;

public class DesfazerAtualizacaoAnuncioCommand implements Command<Anuncio> {

    private final AnuncioController receptor;
    private final AnuncioCaretaker caretaker;

    public DesfazerAtualizacaoAnuncioCommand(AnuncioController receptor, AnuncioCaretaker caretaker) {
        this.receptor = receptor;
        this.caretaker = caretaker;
    }

    @Override
    public Anuncio executar() {
        AnuncioMemento memento = caretaker.recuperar()
                .orElseThrow(() -> new DesfazerException("Não há atualização de anúncio para desfazer"));

        Anuncio anuncio = receptor.buscarAnuncioPorId(memento.getAnuncioId())
                .orElseThrow(() -> new DesfazerException(
                        "O anúncio da última atualização não existe mais: " + memento.getAnuncioId()));

        anuncio.restaurar(memento);
        Anuncio restaurado = receptor.atualizarAnuncio(anuncio);

        caretaker.limpar();
        return restaurado;
    }

    @Override
    public String descricao() {
        return "Desfazer última atualização de anúncio";
    }
}
