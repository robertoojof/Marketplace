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
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN);

        repository.salvar(usuario);
        List<User> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Maria Souza", resultado.get(0).getName());
    }

    @Test
    void buscarTodos_deve_retornar_lista_imutavel() {
        repository.salvar(new User(UUID.randomUUID(), "teste", "000.000.000-00", "Teste", "t@t.com", "Senha@2024!", Role.USER));

        List<User> resultado = repository.buscarTodos();

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(
                new User(UUID.randomUUID(), "invasor", "999.999.999-99", "Invasor", "x@x.com", "Senha@2024!", Role.USER)));
    }

    @Test
    void deve_gerar_uuid_quando_id_nulo() {
        User usuario = new User(null, "semid", "111.222.333-44", "Sem ID", "semid@email.com", "Senha@2024!", Role.USER);

        repository.salvar(usuario);

        assertEquals(1, repository.buscarTodos().size());
        assertEquals("Sem ID", repository.buscarTodos().get(0).getName());
        // UUID deve ter sido gerado pelo repositório
        var id = repository.buscarTodos().get(0).getId();
        assertEquals(36, id.toString().length());
    }
}
