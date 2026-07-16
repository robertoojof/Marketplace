package com.mps.shared.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class UserFacadeTest {

    @AfterEach
    void limpar() {
        UserFacade.reset();
    }

    @Test
    void getInstance_deve_retornar_sempre_a_mesma_instancia() {
        UserFacade a = UserFacade.getInstance(false);
        UserFacade b = UserFacade.getInstance(false);

        assertSame(a, b);
    }

    @Test
    void contarUsuarios_deve_contar_apenas_ativos() {
        UserFacade facade = UserFacade.getInstance(false);
        User ativo = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        User seraRemovido = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.USER, true);
        facade.adicionarUsuario(ativo);
        facade.adicionarUsuario(seraRemovido);

        facade.removerUsuario(seraRemovido.getId());

        assertEquals(1, facade.contarUsuarios());
        assertTrue(facade.buscarUsuarioPorId(seraRemovido.getId()).isPresent());
    }
}
