package com.mps.produtos.domain.catalogo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mps.produtos.domain.Produto;

class CategoriaCatalogoTest {

    private static ItemCatalogo item(String nome, boolean ativo) {
        return new ItemCatalogo(new Produto(UUID.randomUUID(), nome, "descrição", ativo));
    }

    @Test
    void contarProdutos_deve_somar_recursivamente_todos_os_niveis() {
        CategoriaCatalogo raiz = new CategoriaCatalogo("Catálogo");
        CategoriaCatalogo higiene = new CategoriaCatalogo("Higiene");
        CategoriaCatalogo sabonetes = new CategoriaCatalogo("Sabonetes");

        sabonetes.adicionar(item("Dove", true));
        sabonetes.adicionar(item("Lux", true));
        higiene.adicionar(sabonetes);
        higiene.adicionar(item("Shampoo Seda", true));
        raiz.adicionar(higiene);
        raiz.adicionar(item("Caderno", true));

        assertEquals(4, raiz.contarProdutos());
        assertEquals(3, higiene.contarProdutos());
        assertEquals(2, sabonetes.contarProdutos());
    }

    @Test
    void contarProdutos_deve_retornar_zero_quando_categoria_vazia() {
        assertEquals(0, new CategoriaCatalogo("Vazia").contarProdutos());
    }

    @Test
    void exibir_deve_indentar_a_hierarquia_a_partir_do_nivel_informado() {
        CategoriaCatalogo raiz = new CategoriaCatalogo("Catálogo");
        CategoriaCatalogo higiene = new CategoriaCatalogo("Higiene");
        higiene.adicionar(item("Dove", true));
        raiz.adicionar(higiene);

        String[] linhas = raiz.exibir(0).split(System.lineSeparator());

        assertEquals("+ Catálogo (1 produto)", linhas[0]);
        assertEquals("   + Higiene (1 produto)", linhas[1]);
        assertEquals("      - Dove", linhas[2]);
    }

    @Test
    void exibir_deve_marcar_produto_inativo() {
        CategoriaCatalogo raiz = new CategoriaCatalogo("Catálogo");
        raiz.adicionar(item("Dove", false));

        assertTrue(raiz.exibir(0).contains("- Dove (inativo)"));
    }

    @Test
    void folha_deve_recusar_a_adicao_de_filhos() {
        ComponenteCatalogo folha = item("Dove", true);

        assertThrows(UnsupportedOperationException.class, () -> folha.adicionar(item("Lux", true)));
    }

    @Test
    void folha_nao_deve_possuir_filhos() {
        assertTrue(item("Dove", true).getFilhos().isEmpty());
    }

    @Test
    void remover_deve_excluir_o_componente_da_categoria() {
        CategoriaCatalogo raiz = new CategoriaCatalogo("Catálogo");
        ItemCatalogo dove = item("Dove", true);
        raiz.adicionar(dove);
        raiz.adicionar(item("Lux", true));

        raiz.remover(dove);

        assertEquals(1, raiz.contarProdutos());
    }

    @Test
    void obterOuCriarSubcategoria_deve_reaproveitar_a_subcategoria_existente() {
        CategoriaCatalogo raiz = new CategoriaCatalogo("Catálogo");

        CategoriaCatalogo primeira = raiz.obterOuCriarSubcategoria("Higiene");
        CategoriaCatalogo segunda = raiz.obterOuCriarSubcategoria("Higiene");

        assertEquals(primeira, segunda);
        assertEquals(1, raiz.getFilhos().size());
    }

    @Test
    void getFilhos_deve_devolver_copia_imutavel() {
        CategoriaCatalogo raiz = new CategoriaCatalogo("Catálogo");
        raiz.adicionar(item("Dove", true));

        assertThrows(UnsupportedOperationException.class, () -> raiz.getFilhos().clear());
    }
}
