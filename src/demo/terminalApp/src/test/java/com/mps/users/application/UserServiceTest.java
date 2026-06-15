package com.mps.users.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.users.domain.Role;
import com.mps.users.domain.User;
import com.mps.users.infrastructure.InMemoryUserRepository;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void configurar() {
        userService = new UserService(new InMemoryUserRepository());
    }

    @Test
    void deve_adicionar_usuario_e_retornar_na_listagem() {
        User usuario = new User(UUID.randomUUID(), "123.456.789-00", "João Silva", "joao@email.com", "senha123", Role.USER);

        userService.adicionarUsuario(usuario);
        List<User> resultado = userService.listarUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getName());
        assertEquals("joao@email.com", resultado.get(0).getEmail());
    }

    @Test
    void listarUsuarios_deve_retornar_lista_vazia_quando_sem_usuarios() {
        assertTrue(userService.listarUsuarios().isEmpty());
    }
}
