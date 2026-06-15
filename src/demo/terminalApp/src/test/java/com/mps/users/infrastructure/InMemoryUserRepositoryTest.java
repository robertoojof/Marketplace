package com.mps.users.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void configurar() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void deve_salvar_e_retornar_usuario() {
        User usuario = new User(UUID.randomUUID(), "111.222.333-44", "Maria Souza", "maria@email.com", "senha", Role.ADMIN);

        repository.salvar(usuario);
        List<User> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Maria Souza", resultado.get(0).getName());
    }

    @Test
    void buscarTodos_deve_retornar_lista_imutavel() {
        repository.salvar(new User(UUID.randomUUID(), "000.000.000-00", "Teste", "t@t.com", "pw", Role.USER));

        List<User> resultado = repository.buscarTodos();

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(
                new User(UUID.randomUUID(), "999.999.999-99", "Invasor", "x@x.com", "pw", Role.USER)));
    }
}
