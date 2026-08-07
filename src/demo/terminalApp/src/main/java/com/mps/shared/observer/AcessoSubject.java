package com.mps.shared.observer;

import java.util.ArrayList;
import java.util.List;

public class AcessoSubject {

    private final List<AcessoObserver> observadores = new ArrayList<>();

    public void registrar(AcessoObserver observador) {
        observadores.add(observador);
    }

    public void remover(AcessoObserver observador) {
        observadores.remove(observador);
    }

    public List<AcessoObserver> getObservadores() {
        return List.copyOf(observadores);
    }

    public void notificar(EventoDeAcesso evento) {
        for (AcessoObserver observador : List.copyOf(observadores)) {
            observador.aoOcorrer(evento);
        }
    }
}
