package com.mps.anuncios.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.exception.ValidacaoAnuncioException;
import com.mps.anuncios.infrastructure.InMemoryAnuncioRepository;
import com.mps.produtos.domain.Produto;
import com.mps.produtos.infrastructure.InMemoryProdutoRepository;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;
import com.mps.users.infrastructure.InMemoryUserRepository;

class AnuncioServiceTest {

    private AnuncioService anuncioService;
    private InMemoryProdutoRepository produtoRepository;
    private InMemoryUserRepository userRepository;
    private Produto produto;
    private User vendedor;

    @BeforeEach
    void configurar() {
        produtoRepository = new InMemoryProdutoRepository();
        userRepository = new InMemoryUserRepository();
        anuncioService = new AnuncioService(new InMemoryAnuncioRepository(), produtoRepository, userRepository);

        produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoRepository.salvar(produto);

        vendedor = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        userRepository.salvar(vendedor);
    }

    private Anuncio novoAnuncio(Produto p, User v, BigDecimal preco, int quantidade) {
        return new Anuncio(UUID.randomUUID(), p, v, preco, quantidade, true);
    }

    @Test
    void deve_adicionar_anuncio_valido() {
        Anuncio anuncio = novoAnuncio(produto, vendedor, new BigDecimal("12.90"), 10);

        anuncioService.adicionarAnuncio(anuncio);

        assertEquals(1, anuncioService.listarAnuncios().size());
    }

    @Test
    void deve_lancar_excecao_quando_preco_e_zero() {
        Anuncio anuncio = novoAnuncio(produto, vendedor, BigDecimal.ZERO, 10);

        assertThrows(ValidacaoAnuncioException.class, () -> anuncioService.adicionarAnuncio(anuncio));
    }

    @Test
    void deve_lancar_excecao_quando_quantidade_negativa() {
        Anuncio anuncio = novoAnuncio(produto, vendedor, new BigDecimal("12.90"), -1);

        assertThrows(ValidacaoAnuncioException.class, () -> anuncioService.adicionarAnuncio(anuncio));
    }

    @Test
    void deve_lancar_excecao_quando_produto_nao_existe_no_catalogo() {
        Produto produtoFantasma = new Produto(UUID.randomUUID(), "Fantasma", "N/A", true);
        Anuncio anuncio = novoAnuncio(produtoFantasma, vendedor, new BigDecimal("12.90"), 10);

        ValidacaoAnuncioException ex = assertThrows(ValidacaoAnuncioException.class,
                () -> anuncioService.adicionarAnuncio(anuncio));
        assertTrue(ex.getErros().stream().anyMatch(e -> e.contains("Produto informado não existe")));
    }

    @Test
    void deve_lancar_excecao_quando_vendedor_nao_existe() {
        User vendedorFantasma = new User(UUID.randomUUID(), "fantasma", "000.000.000-00", "Fantasma", "f@f.com", "Senha@2024!", Role.USER, true);
        Anuncio anuncio = novoAnuncio(produto, vendedorFantasma, new BigDecimal("12.90"), 10);

        ValidacaoAnuncioException ex = assertThrows(ValidacaoAnuncioException.class,
                () -> anuncioService.adicionarAnuncio(anuncio));
        assertTrue(ex.getErros().stream().anyMatch(e -> e.contains("Vendedor informado não existe")));
    }

    @Test
    void dois_vendedores_podem_anunciar_o_mesmo_produto_com_preco_e_estoque_diferentes() {
        User outroVendedor = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.USER, true);
        userRepository.salvar(outroVendedor);

        anuncioService.adicionarAnuncio(novoAnuncio(produto, vendedor, new BigDecimal("12.90"), 10));
        anuncioService.adicionarAnuncio(novoAnuncio(produto, outroVendedor, new BigDecimal("15.50"), 3));

        assertEquals(2, anuncioService.listarAnuncios().size());
    }

    @Test
    void desativarAnunciosDoVendedor_deve_desativar_todos_os_anuncios_dele() {
        Anuncio anuncio1 = novoAnuncio(produto, vendedor, new BigDecimal("12.90"), 10);
        Anuncio anuncio2 = novoAnuncio(produto, vendedor, new BigDecimal("9.90"), 5);
        anuncioService.adicionarAnuncio(anuncio1);
        anuncioService.adicionarAnuncio(anuncio2);

        anuncioService.desativarAnunciosDoVendedor(vendedor.getId());

        assertFalse(anuncioService.buscarAnuncioPorId(anuncio1.getId()).get().isAtivo());
        assertFalse(anuncioService.buscarAnuncioPorId(anuncio2.getId()).get().isAtivo());
    }
}
