package com.mps.anuncios.application;

import java.util.Optional;

import com.mps.anuncios.domain.AnuncioMemento;

public class AnuncioCaretaker {

    private AnuncioMemento ultimoEstado;

    public void guardar(AnuncioMemento memento) {
        this.ultimoEstado = memento;
    }

    public Optional<AnuncioMemento> recuperar() {
        return Optional.ofNullable(ultimoEstado);
    }

    public boolean possuiEstadoSalvo() {
        return ultimoEstado != null;
    }

    public void limpar() {
        this.ultimoEstado = null;
    }
}
