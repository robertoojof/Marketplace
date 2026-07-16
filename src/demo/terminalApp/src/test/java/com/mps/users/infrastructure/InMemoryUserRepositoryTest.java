package com.mps.users.infrastructure;

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

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void configurar() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void deve_salvar_e_retornar_usuario() {
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN, true);

        repository.salvar(usuario);
        List<User> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Maria Souza", resultado.get(0).getName());
    }

    @Test
    void buscarTodos_deve_retornar_lista_imutavel() {
        repository.salvar(new User(UUID.randomUUID(), "teste", "000.000.000-00", "Teste", "t@t.com", "Senha@2024!", Role.USER, true));

        List<User> resultado = repository.buscarTodos();

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(
                new User(UUID.randomUUID(), "invasor", "999.999.999-99", "Invasor", "x@x.com", "Senha@2024!", Role.USER, true)));
    }

    @Test
    void deve_gerar_uuid_quando_id_nulo() {
        User usuario = new User(null, "semid", "111.222.333-44", "Sem ID", "semid@email.com", "Senha@2024!", Role.USER, true);

        repository.salvar(usuario);

        assertEquals(1, repository.buscarTodos().size());
        assertEquals("Sem ID", repository.buscarTodos().get(0).getName());
        // UUID deve ter sido gerado pelo repositório
        var id = repository.buscarTodos().get(0).getId();
        assertEquals(36, id.toString().length());
    }

    @Test
    void buscarPorId_deve_retornar_usuario_quando_existe() {
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN, true);
        repository.salvar(usuario);

        Optional<User> resultado = repository.buscarPorId(usuario.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Maria Souza", resultado.get().getName());
    }

    @Test
    void buscarPorId_deve_retornar_optional_vazio_quando_nao_existe() {
        Optional<User> resultado = repository.buscarPorId(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void atualizar_deve_substituir_dados_do_usuario() {
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN, true);
        repository.salvar(usuario);

        usuario.setName("Maria Oliveira");
        User atualizado = repository.atualizar(usuario);

        assertEquals("Maria Oliveira", atualizado.getName());
        assertEquals("Maria Oliveira", repository.buscarPorId(usuario.getId()).get().getName());
    }

    @Test
    void atualizar_deve_lancar_excecao_quando_usuario_nao_existe() {
        User usuario = new User(UUID.randomUUID(), "fantasma", "000.000.000-00", "Fantasma", "f@f.com", "Senha@2024!", Role.USER, true);

        assertThrows(RepositorioException.class, () -> repository.atualizar(usuario));
    }

    @Test
    void deletar_deve_marcar_usuario_como_inativo() {
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN, true);
        repository.salvar(usuario);

        repository.deletar(usuario.getId());

        assertFalse(repository.buscarPorId(usuario.getId()).get().isAtivo());
        assertEquals(1, repository.buscarTodos().size());
    }

    @Test
    void deletar_deve_lancar_excecao_quando_usuario_nao_existe() {
        assertThrows(RepositorioException.class, () -> repository.deletar(UUID.randomUUID()));
    }

    @Test
    void buscarPorLogin_deve_retornar_usuario_quando_existe() {
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN, true);
        repository.salvar(usuario);

        Optional<User> resultado = repository.buscarPorLogin("mariasouz");

        assertTrue(resultado.isPresent());
        assertEquals("Maria Souza", resultado.get().getName());
    }

    @Test
    void buscarPorLogin_deve_retornar_vazio_quando_nao_existe() {
        assertTrue(repository.buscarPorLogin("inexistente").isEmpty());
    }

    @Test
    void reativar_deve_marcar_usuario_como_ativo() {
        User usuario = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.ADMIN, true);
        repository.salvar(usuario);
        repository.deletar(usuario.getId());

        repository.reativar(usuario.getId());

        assertTrue(repository.buscarPorId(usuario.getId()).get().isAtivo());
    }

    @Test
    void reativar_deve_lancar_excecao_quando_usuario_nao_existe() {
        assertThrows(RepositorioException.class, () -> repository.reativar(UUID.randomUUID()));
    }
}
