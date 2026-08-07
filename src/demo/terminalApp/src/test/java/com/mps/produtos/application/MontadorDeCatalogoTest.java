package com.mps.produtos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mps.produtos.domain.Produto;
import com.mps.produtos.domain.catalogo.CategoriaCatalogo;

class MontadorDeCatalogoTest {

    private static final String RAIZ = "Catálogo";

    private final MontadorDeCatalogo montador = new MontadorDeCatalogo();

    private static Produto produto(String nome, String categoria) {
        return new Produto(UUID.randomUUID(), nome, "descrição", categoria, true);
    }

    @Test
    void montar_deve_criar_um_nivel_por_trecho_da_categoria() {
        CategoriaCatalogo catalogo = montador.montar(RAIZ,
                List.of(produto("Dove", "Higiene/Sabonetes")));

        String[] linhas = catalogo.exibir(0).split(System.lineSeparator());

        assertEquals(4, linhas.length);
        assertEquals("+ Catálogo (1 produto)", linhas[0]);
        assertEquals("   + Higiene (1 produto)", linhas[1]);
        assertEquals("      + Sabonetes (1 produto)", linhas[2]);
        assertEquals("         - Dove", linhas[3]);
    }

    @Test
    void montar_deve_agrupar_produtos_que_compartilham_o_mesmo_caminho() {
        CategoriaCatalogo catalogo = montador.montar(RAIZ, List.of(
                produto("Dove", "Higiene/Sabonetes"),
                produto("Lux", "Higiene/Sabonetes"),
                produto("Seda", "Higiene/Shampoos")));

        assertEquals(3, catalogo.contarProdutos());
        assertEquals(1, catalogo.getFilhos().size());
        assertEquals("Higiene", catalogo.getFilhos().get(0).getNome());
        assertEquals(2, catalogo.getFilhos().get(0).getFilhos().size());
    }

    @Test
    void montar_deve_usar_categoria_padrao_quando_produto_nao_informa_categoria() {
        CategoriaCatalogo catalogo = montador.montar(RAIZ, List.of(
                produto("Dove", null),
                produto("Lux", "   ")));

        assertEquals(1, catalogo.getFilhos().size());
        assertEquals(Produto.CATEGORIA_PADRAO, catalogo.getFilhos().get(0).getNome());
        assertEquals(2, catalogo.contarProdutos());
    }

    @Test
    void montar_deve_ignorar_separadores_sobrando_na_categoria() {
        CategoriaCatalogo catalogo = montador.montar(RAIZ,
                List.of(produto("Dove", "/Higiene//Sabonetes/")));

        assertEquals("Higiene", catalogo.getFilhos().get(0).getNome());
        assertEquals("Sabonetes", catalogo.getFilhos().get(0).getFilhos().get(0).getNome());
    }

    @Test
    void montar_deve_devolver_catalogo_vazio_quando_nao_ha_produtos() {
        CategoriaCatalogo catalogo = montador.montar(RAIZ, List.of());

        assertEquals(0, catalogo.contarProdutos());
        assertEquals(RAIZ, catalogo.getNome());
    }
}
