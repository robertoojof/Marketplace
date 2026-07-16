package com.mps.shared.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.Anuncio;
import com.mps.produtos.domain.Produto;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class AnuncioFacadeTest {

    @AfterEach
    void limpar() {
        AnuncioFacade.reset();
        ProdutoFacade.reset();
        UserFacade.reset();
    }

    @Test
    void getInstance_deve_retornar_sempre_a_mesma_instancia() {
        AnuncioFacade a = AnuncioFacade.getInstance(false);
        AnuncioFacade b = AnuncioFacade.getInstance(false);

        assertSame(a, b);
    }

    @Test
    void contarAnuncios_deve_contar_apenas_ativos() {
        ProdutoFacade produtoFacade = ProdutoFacade.getInstance(false);
        UserFacade userFacade = UserFacade.getInstance(false);
        AnuncioFacade anuncioFacade = AnuncioFacade.getInstance(false);

        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoFacade.adicionarProduto(produto);
        User vendedor = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userFacade.adicionarUsuario(vendedor);

        Anuncio ativo = new Anuncio(UUID.randomUUID(), produto, vendedor, new BigDecimal("12.90"), 10, true);
        Anuncio seraRemovido = new Anuncio(UUID.randomUUID(), produto, vendedor, new BigDecimal("9.90"), 5, true);
        anuncioFacade.adicionarAnuncio(ativo);
        anuncioFacade.adicionarAnuncio(seraRemovido);

        anuncioFacade.removerAnuncio(seraRemovido.getId());

        assertEquals(1, anuncioFacade.contarAnuncios());
    }
}
