package com.mps.acessos.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.TipoAcesso;

class InMemoryAcessoLogRepositoryTest {

    private InMemoryAcessoLogRepository repository;

    @BeforeEach
    void configurar() {
        repository = new InMemoryAcessoLogRepository();
    }

    @Test
    void deve_salvar_e_retornar_log_de_acesso() {
        AcessoLog log = new AcessoLog(UUID.randomUUID(), UUID.randomUUID(), TipoAcesso.CRIACAO, Instant.now());

        repository.salvar(log);
        List<AcessoLog> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals(TipoAcesso.CRIACAO, resultado.get(0).getAcao());
    }

    @Test
    void buscarTodos_deve_retornar_lista_imutavel() {
        repository.salvar(new AcessoLog(UUID.randomUUID(), UUID.randomUUID(), TipoAcesso.BUSCA, Instant.now()));

        List<AcessoLog> resultado = repository.buscarTodos();

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(
                new AcessoLog(UUID.randomUUID(), UUID.randomUUID(), TipoAcesso.BUSCA, Instant.now())));
    }
}
