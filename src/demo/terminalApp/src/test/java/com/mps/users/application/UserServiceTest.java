package com.mps.users.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.shared.exception.RepositorioException;
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
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);

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
        User usuario = new User(UUID.randomUUID(), "joao123", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);

        ValidacaoUsuarioException ex = assertThrows(ValidacaoUsuarioException.class,
                () -> userService.adicionarUsuario(usuario));

        assertTrue(ex.getErros().stream().anyMatch(e -> e.contains("Login não pode conter números")));
    }

    @Test
    void deve_lancar_excecao_quando_login_excede_doze_caracteres() {
        User usuario = new User(UUID.randomUUID(), "loginmuitolongo", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);

        ValidacaoUsuarioException ex = assertThrows(ValidacaoUsuarioException.class,
                () -> userService.adicionarUsuario(usuario));

        assertTrue(ex.getErros().stream().anyMatch(e -> e.contains("máximo")));
    }

    @Test
    void deve_lancar_excecao_quando_senha_invalida() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "senhafraca", Role.USER, true);

        ValidacaoUsuarioException ex = assertThrows(ValidacaoUsuarioException.class,
                () -> userService.adicionarUsuario(usuario));

        assertTrue(ex.getErros().size() >= 1);
    }

    @Test
    void buscarUsuarioPorId_deve_retornar_usuario_quando_existe() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userService.adicionarUsuario(usuario);

        Optional<User> resultado = userService.buscarUsuarioPorId(usuario.getId());

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getName());
    }

    @Test
    void buscarUsuarioPorId_deve_retornar_vazio_quando_nao_existe() {
        Optional<User> resultado = userService.buscarUsuarioPorId(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deve_atualizar_usuario_existente() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userService.adicionarUsuario(usuario);

        usuario.setName("João Souza");
        User atualizado = userService.atualizarUsuario(usuario);

        assertEquals("João Souza", atualizado.getName());
        assertEquals("João Souza", userService.buscarUsuarioPorId(usuario.getId()).get().getName());
    }

    @Test
    void atualizarUsuario_deve_lancar_excecao_quando_dados_invalidos() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userService.adicionarUsuario(usuario);

        usuario.setLogin("joao123");

        assertThrows(ValidacaoUsuarioException.class, () -> userService.atualizarUsuario(usuario));
    }

    @Test
    void deve_remover_usuario_existente() {
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userService.adicionarUsuario(usuario);

        userService.removerUsuario(usuario.getId());

        assertFalse(userService.buscarUsuarioPorId(usuario.getId()).get().isAtivo());
    }

    @Test
    void removerUsuario_deve_lancar_excecao_quando_nao_existe() {
        assertThrows(RepositorioException.class, () -> userService.removerUsuario(UUID.randomUUID()));
    }
}
