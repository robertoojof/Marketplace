package com.mps.produtos.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.RepositorioException;

class InMemoryProdutoRepositoryTest {

    private InMemoryProdutoRepository repository;

    @BeforeEach
    void configurar() {
        repository = new InMemoryProdutoRepository();
    }

    @Test
    void deve_salvar_e_retornar_produto() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);

        repository.salvar(produto);
        List<Produto> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Sabonete Dove", resultado.get(0).getNome());
    }

    @Test
    void deve_gerar_uuid_quando_id_nulo() {
        Produto produto = new Produto(null, "Sabonete Dove", "Sabonete hidratante", true);

        repository.salvar(produto);

        assertEquals(36, repository.buscarTodos().get(0).getId().toString().length());
    }

    @Test
    void buscarPorId_deve_retornar_produto_quando_existe() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        repository.salvar(produto);

        Optional<Produto> resultado = repository.buscarPorId(produto.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Sabonete Dove", resultado.get().getNome());
    }

    @Test
    void buscarPorId_deve_retornar_vazio_quando_nao_existe() {
        assertTrue(repository.buscarPorId(UUID.randomUUID()).isEmpty());
    }

    @Test
    void atualizar_deve_substituir_dados_do_produto() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        repository.salvar(produto);

        produto.setNome("Sabonete Dove Original");
        Produto atualizado = repository.atualizar(produto);

        assertEquals("Sabonete Dove Original", atualizado.getNome());
    }

    @Test
    void atualizar_deve_lancar_excecao_quando_produto_nao_existe() {
        Produto produto = new Produto(UUID.randomUUID(), "Fantasma", "N/A", true);

        assertThrows(RepositorioException.class, () -> repository.atualizar(produto));
    }

    @Test
    void deletar_deve_marcar_produto_como_inativo() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        repository.salvar(produto);

        repository.deletar(produto.getId());

        assertFalse(repository.buscarPorId(produto.getId()).get().isAtivo());
    }

    @Test
    void deletar_deve_lancar_excecao_quando_produto_nao_existe() {
        assertThrows(RepositorioException.class, () -> repository.deletar(UUID.randomUUID()));
    }

    @Test
    void reativar_deve_marcar_produto_como_ativo() {
        Produto produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        repository.salvar(produto);
        repository.deletar(produto.getId());

        repository.reativar(produto.getId());

        assertTrue(repository.buscarPorId(produto.getId()).get().isAtivo());
    }
}
