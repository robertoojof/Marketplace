package com.mps.shared.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.TipoAcesso;
import com.mps.shared.factory.InMemoryRepositoryFactory;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class UserFacadeTest {

    @AfterEach
    void limpar() {
        UserFacade.reset();
    }

    @Test
    void getInstance_deve_retornar_sempre_a_mesma_instancia() {
        UserFacade a = UserFacade.getInstance(new InMemoryRepositoryFactory());
        UserFacade b = UserFacade.getInstance(new InMemoryRepositoryFactory());

        assertSame(a, b);
    }

    @Test
    void contarUsuarios_deve_contar_apenas_ativos() {
        UserFacade facade = UserFacade.getInstance(new InMemoryRepositoryFactory());
        User ativo = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        User seraRemovido = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.USER, true);
        facade.adicionarUsuario(ativo);
        facade.adicionarUsuario(seraRemovido);

        facade.removerUsuario(seraRemovido.getId());

        assertEquals(1, facade.contarUsuarios());
        assertTrue(facade.buscarUsuarioPorId(seraRemovido.getId()).isPresent());
    }

    @Test
    void deve_registrar_acesso_a_cada_operacao_de_crud() {
        UserFacade facade = UserFacade.getInstance(new InMemoryRepositoryFactory());
        User usuario = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);

        facade.adicionarUsuario(usuario);
        facade.buscarUsuarioPorId(usuario.getId());
        usuario.setName("João Souza");
        facade.atualizarUsuario(usuario);
        facade.removerUsuario(usuario.getId());
        facade.reativarUsuario(usuario.getId(), "joaosilva", "Senha@2024!");

        List<AcessoLog> acessos = facade.listarAcessos();

        assertEquals(5, acessos.size());
        assertTrue(acessos.stream().allMatch(a -> a.getUsuarioId().equals(usuario.getId())));
        assertTrue(acessos.stream().anyMatch(a -> a.getAcao() == TipoAcesso.CRIACAO));
        assertTrue(acessos.stream().anyMatch(a -> a.getAcao() == TipoAcesso.BUSCA));
        assertTrue(acessos.stream().anyMatch(a -> a.getAcao() == TipoAcesso.ATUALIZACAO));
        assertTrue(acessos.stream().anyMatch(a -> a.getAcao() == TipoAcesso.REMOCAO));
        assertTrue(acessos.stream().anyMatch(a -> a.getAcao() == TipoAcesso.REATIVACAO));
    }
}
