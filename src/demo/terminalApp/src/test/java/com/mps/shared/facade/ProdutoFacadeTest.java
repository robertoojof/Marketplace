package com.mps.shared.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.mps.produtos.domain.Produto;
import com.mps.shared.factory.InMemoryRepositoryFactory;

class ProdutoFacadeTest {

    @AfterEach
    void limpar() {
        ProdutoFacade.reset();
    }

    @Test
    void getInstance_deve_retornar_sempre_a_mesma_instancia() {
        ProdutoFacade a = ProdutoFacade.getInstance(new InMemoryRepositoryFactory());
        ProdutoFacade b = ProdutoFacade.getInstance(new InMemoryRepositoryFactory());

        assertSame(a, b);
    }

    @Test
    void contarProdutos_deve_contar_apenas_ativos() {
        ProdutoFacade facade = ProdutoFacade.getInstance(new InMemoryRepositoryFactory());
        Produto ativo = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        Produto seraRemovido = new Produto(UUID.randomUUID(), "Shampoo", "Shampoo anticaspa", true);
        facade.adicionarProduto(ativo);
        facade.adicionarProduto(seraRemovido);

        facade.removerProduto(seraRemovido.getId());

        assertEquals(1, facade.contarProdutos());
    }
}
