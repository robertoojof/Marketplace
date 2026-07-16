package com.mps.shared.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.Anuncio;
import com.mps.produtos.domain.Produto;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class FacadeSingletonControllerTest {

    @AfterEach
    void limpar() {
        FacadeSingletonController.reset();
    }

    @Test
    void getInstance_deve_retornar_sempre_a_mesma_instancia() {
        FacadeSingletonController a = FacadeSingletonController.getInstance(false);
        FacadeSingletonController b = FacadeSingletonController.getInstance(false);

        assertSame(a, b);
    }

    @Test
    void contarEntidadesCadastradas_deve_somar_usuarios_produtos_e_anuncios() {
        FacadeSingletonController facade = FacadeSingletonController.getInstance(false);
        UserFacade userFacade = UserFacade.getInstance();
        ProdutoFacade produtoFacade = ProdutoFacade.getInstance();
        AnuncioFacade anuncioFacade = AnuncioFacade.getInstance();

        User vendedor = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userFacade.adicionarUsuario(vendedor);

        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoFacade.adicionarProduto(produto);

        Anuncio anuncio = new Anuncio(UUID.randomUUID(), produto, vendedor, new BigDecimal("12.90"), 10, true);
        anuncioFacade.adicionarAnuncio(anuncio);

        assertEquals(1, facade.contarUsuarios());
        assertEquals(1, facade.contarProdutos());
        assertEquals(1, facade.contarAnuncios());
        assertEquals(3, facade.contarEntidadesCadastradas());
    }

    @Test
    void removerUsuario_deve_cascatear_desativacao_dos_anuncios_do_vendedor() {
        FacadeSingletonController facade = FacadeSingletonController.getInstance(false);
        UserFacade userFacade = UserFacade.getInstance();
        ProdutoFacade produtoFacade = ProdutoFacade.getInstance();
        AnuncioFacade anuncioFacade = AnuncioFacade.getInstance();

        User vendedor = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userFacade.adicionarUsuario(vendedor);

        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoFacade.adicionarProduto(produto);

        Anuncio anuncio1 = new Anuncio(UUID.randomUUID(), produto, vendedor, new BigDecimal("12.90"), 10, true);
        Anuncio anuncio2 = new Anuncio(UUID.randomUUID(), produto, vendedor, new BigDecimal("9.90"), 5, true);
        anuncioFacade.adicionarAnuncio(anuncio1);
        anuncioFacade.adicionarAnuncio(anuncio2);

        facade.removerUsuario(vendedor.getId());

        assertFalse(userFacade.buscarUsuarioPorId(vendedor.getId()).get().isAtivo());
        assertEquals(0, facade.contarAnuncios());
    }
}
