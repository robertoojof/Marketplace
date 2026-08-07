package com.mps.shared.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mps.acessos.domain.TipoAcesso;

class AcessoSubjectTest {

    private static class ObservadorEspiao implements AcessoObserver {

        private final List<EventoDeAcesso> recebidos = new ArrayList<>();

        @Override
        public void aoOcorrer(EventoDeAcesso evento) {
            recebidos.add(evento);
        }
    }

    private static EventoDeAcesso evento() {
        return EventoDeAcesso.agora(UUID.randomUUID(), TipoAcesso.CRIACAO);
    }

    @Test
    void notificar_deve_avisar_todos_os_observadores_registrados() {
        AcessoSubject sujeito = new AcessoSubject();
        ObservadorEspiao primeiro = new ObservadorEspiao();
        ObservadorEspiao segundo = new ObservadorEspiao();
        sujeito.registrar(primeiro);
        sujeito.registrar(segundo);

        EventoDeAcesso evento = evento();
        sujeito.notificar(evento);

        assertEquals(List.of(evento), primeiro.recebidos);
        assertEquals(List.of(evento), segundo.recebidos);
    }

    @Test
    void notificar_nao_deve_avisar_observador_removido() {
        AcessoSubject sujeito = new AcessoSubject();
        ObservadorEspiao observador = new ObservadorEspiao();
        sujeito.registrar(observador);

        sujeito.remover(observador);
        sujeito.notificar(evento());

        assertTrue(observador.recebidos.isEmpty());
    }

    @Test
    void notificar_sem_observadores_nao_deve_falhar() {
        new AcessoSubject().notificar(evento());
    }

    @Test
    void notificar_deve_tolerar_observador_que_se_desregistra_durante_o_aviso() {
        AcessoSubject sujeito = new AcessoSubject();
        ObservadorEspiao permanente = new ObservadorEspiao();
        AcessoObserver efemero = new AcessoObserver() {
            @Override
            public void aoOcorrer(EventoDeAcesso evento) {
                sujeito.remover(this);
            }
        };
        sujeito.registrar(efemero);
        sujeito.registrar(permanente);

        sujeito.notificar(evento());

        assertEquals(1, permanente.recebidos.size());
        assertEquals(List.of(permanente), sujeito.getObservadores());
    }

    @Test
    void getObservadores_deve_devolver_copia_imutavel() {
        AcessoSubject sujeito = new AcessoSubject();
        sujeito.registrar(new ObservadorEspiao());

        List<AcessoObserver> observadores = sujeito.getObservadores();

        assertEquals(1, observadores.size());
        assertTrue(observadores.getClass().getName().contains("ImmutableCollections"));
    }
}
