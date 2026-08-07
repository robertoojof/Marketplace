package com.mps.produtos.application;

import java.util.Arrays;
import java.util.List;

import com.mps.produtos.domain.Produto;
import com.mps.produtos.domain.catalogo.CategoriaCatalogo;
import com.mps.produtos.domain.catalogo.ItemCatalogo;

public class MontadorDeCatalogo {

    public static final String SEPARADOR_DE_NIVEIS = "/";

    public CategoriaCatalogo montar(String nomeDaRaiz, List<Produto> produtos) {
        CategoriaCatalogo raiz = new CategoriaCatalogo(nomeDaRaiz);
        for (Produto produto : produtos) {
            categoriaDe(raiz, produto).adicionar(new ItemCatalogo(produto));
        }
        return raiz;
    }

    private CategoriaCatalogo categoriaDe(CategoriaCatalogo raiz, Produto produto) {
        CategoriaCatalogo atual = raiz;
        for (String nivel : niveisDe(produto.getCategoria())) {
            atual = atual.obterOuCriarSubcategoria(nivel);
        }
        return atual;
    }

    private List<String> niveisDe(String categoria) {
        if (categoria == null) {
            return List.of(Produto.CATEGORIA_PADRAO);
        }

        List<String> niveis = Arrays.stream(categoria.split(SEPARADOR_DE_NIVEIS))
                .map(String::trim)
                .filter(nivel -> !nivel.isEmpty())
                .toList();

        return niveis.isEmpty() ? List.of(Produto.CATEGORIA_PADRAO) : niveis;
    }
}
