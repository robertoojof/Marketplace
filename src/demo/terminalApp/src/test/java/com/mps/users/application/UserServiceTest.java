package com.mps.users.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.users.domain.Role;
import com.mps.users.domain.User;
import com.mps.users.domain.exception.ValidacaoUsuarioException;
import com.mps.users.infrastructure.InMemoryUserRepository;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void configurar() {
        userService = new UserService(new InMemoryUserRepository());
    }

    @Test
    void deve_adicionar_usuario_e_retornar_na_listagem() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER);

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

    @Test
    void deve_lancar_excecao_quando_login_contem_numero() {
        User usuario = new User(UUID.randomUUID(), "joao123", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER);

        ValidacaoUsuarioException ex = assertThrows(ValidacaoUsuarioException.class,
                () -> userService.adicionarUsuario(usuario));

        assertTrue(ex.getErros().stream().anyMatch(e -> e.contains("Login não pode conter números")));
    }

    @Test
    void deve_lancar_excecao_quando_login_excede_doze_caracteres() {
        User usuario = new User(UUID.randomUUID(), "loginmuitolongo", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER);

        ValidacaoUsuarioException ex = assertThrows(ValidacaoUsuarioException.class,
                () -> userService.adicionarUsuario(usuario));

        assertTrue(ex.getErros().stream().anyMatch(e -> e.contains("máximo")));
    }

    @Test
    void deve_lancar_excecao_quando_senha_invalida() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "senhafraca", Role.USER);

        ValidacaoUsuarioException ex = assertThrows(ValidacaoUsuarioException.class,
                () -> userService.adicionarUsuario(usuario));

        assertTrue(ex.getErros().size() >= 1);
    }
}
