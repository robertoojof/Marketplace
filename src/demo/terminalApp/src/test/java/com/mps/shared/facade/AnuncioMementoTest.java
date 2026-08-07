package com.mps.shared.facade;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.Anuncio;
import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.DesfazerException;
import com.mps.shared.factory.InMemoryRepositoryFactory;
import com.mps.shared.factory.RepositoryFactory;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class AnuncioMementoTest {

    private AnuncioFacade anuncioFacade;
    private Produto produto;
    private User vendedor;

    @BeforeEach
    void preparar() {
        AnuncioFacade.reset();
        ProdutoFacade.reset();
        UserFacade.reset();

        RepositoryFactory factory = new InMemoryRepositoryFactory();
        ProdutoFacade produtoFacade = ProdutoFacade.getInstance(factory);
        UserFacade userFacade = UserFacade.getInstance(factory);
        anuncioFacade = AnuncioFacade.getInstance(factory);

        produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoFacade.adicionarProduto(produto);

        vendedor = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva",
                "joao@email.com", "Senha@2024!", Role.USER, true);
        userFacade.adicionarUsuario(vendedor);
    }

    @AfterEach
    void limpar() {
        AnuncioFacade.reset();
        ProdutoFacade.reset();
        UserFacade.reset();
    }

    private Anuncio anuncioCom(UUID id, String preco, int estoque) {
        return new Anuncio(id, produto, vendedor, new BigDecimal(preco), estoque, true);
    }

    @Test
    void desfazer_deve_restaurar_o_estado_anterior_a_ultima_atualizacao() {
        UUID id = UUID.randomUUID();
        anuncioFacade.adicionarAnuncio(anuncioCom(id, "12.90", 10));

        anuncioFacade.atualizarAnuncio(anuncioCom(id, "20.00", 3));
        Anuncio depoisDaAtualizacao = anuncioFacade.buscarAnuncioPorId(id).orElseThrow();
        assertEquals(new BigDecimal("20.00"), depoisDaAtualizacao.getPreco());
        assertEquals(3, depoisDaAtualizacao.getQuantidadeEmEstoque());

        anuncioFacade.desfazerUltimaAtualizacao();

        Anuncio restaurado = anuncioFacade.buscarAnuncioPorId(id).orElseThrow();
        assertEquals(new BigDecimal("12.90"), restaurado.getPreco());
        assertEquals(10, restaurado.getQuantidadeEmEstoque());
    }

    @Test
    void desfazer_deve_reverter_apenas_a_ultima_de_duas_atualizacoes() {
        UUID id = UUID.randomUUID();
        anuncioFacade.adicionarAnuncio(anuncioCom(id, "12.90", 10));

        anuncioFacade.atualizarAnuncio(anuncioCom(id, "20.00", 3));
        anuncioFacade.atualizarAnuncio(anuncioCom(id, "35.50", 1));

        anuncioFacade.desfazerUltimaAtualizacao();

        Anuncio restaurado = anuncioFacade.buscarAnuncioPorId(id).orElseThrow();
        assertEquals(new BigDecimal("20.00"), restaurado.getPreco());
        assertEquals(3, restaurado.getQuantidadeEmEstoque());
    }

    @Test
    void desfazer_deve_ficar_indisponivel_apos_ser_usado_uma_vez() {
        UUID id = UUID.randomUUID();
        anuncioFacade.adicionarAnuncio(anuncioCom(id, "12.90", 10));
        anuncioFacade.atualizarAnuncio(anuncioCom(id, "20.00", 3));

        assertTrue(anuncioFacade.possuiAtualizacaoParaDesfazer());
        anuncioFacade.desfazerUltimaAtualizacao();

        assertFalse(anuncioFacade.possuiAtualizacaoParaDesfazer());
        assertThrows(DesfazerException.class, () -> anuncioFacade.desfazerUltimaAtualizacao());
    }

    @Test
    void desfazer_sem_atualizacao_previa_deve_falhar() {
        UUID id = UUID.randomUUID();
        anuncioFacade.adicionarAnuncio(anuncioCom(id, "12.90", 10));

        assertFalse(anuncioFacade.possuiAtualizacaoParaDesfazer());
        assertThrows(DesfazerException.class, () -> anuncioFacade.desfazerUltimaAtualizacao());
    }
}
