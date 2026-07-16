package com.mps.produtos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.produtos.domain.Produto;
import com.mps.produtos.domain.exception.ValidacaoProdutoException;
import com.mps.produtos.infrastructure.InMemoryProdutoRepository;

class ProdutoServiceTest {

    private ProdutoService produtoService;

    @BeforeEach
    void configurar() {
        produtoService = new ProdutoService(new InMemoryProdutoRepository());
    }

    @Test
    void deve_adicionar_produto_e_retornar_na_listagem() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);

        produtoService.adicionarProduto(produto);

        assertEquals(1, produtoService.listarProdutos().size());
    }

    @Test
    void deve_lancar_excecao_quando_nome_vazio() {
        Produto produto = new Produto(UUID.randomUUID(), "", "Sabonete hidratante", true);

        assertThrows(ValidacaoProdutoException.class, () -> produtoService.adicionarProduto(produto));
    }

    @Test
    void deve_atualizar_produto_existente() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoService.adicionarProduto(produto);

        produto.setNome("Sabonete Dove Original");
        Produto atualizado = produtoService.atualizarProduto(produto);

        assertEquals("Sabonete Dove Original", atualizado.getNome());
    }

    @Test
    void deve_remover_produto_existente() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoService.adicionarProduto(produto);

        produtoService.removerProduto(produto.getId());

        assertFalse(produtoService.buscarProdutoPorId(produto.getId()).get().isAtivo());
    }

    @Test
    void deve_reativar_produto_existente() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoService.adicionarProduto(produto);
        produtoService.removerProduto(produto.getId());

        produtoService.reativarProduto(produto.getId());

        assertTrue(produtoService.buscarProdutoPorId(produto.getId()).get().isAtivo());
    }
}
