package com.mps.produtos.domain.catalogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaCatalogo implements ComponenteCatalogo {

    private final String nome;
    private final List<ComponenteCatalogo> filhos = new ArrayList<>();

    public CategoriaCatalogo(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public List<ComponenteCatalogo> getFilhos() {
        return List.copyOf(filhos);
    }

    @Override
    public void adicionar(ComponenteCatalogo componente) {
        filhos.add(componente);
    }

    @Override
    public void remover(ComponenteCatalogo componente) {
        filhos.remove(componente);
    }

    @Override
    public int contarProdutos() {
        return filhos.stream().mapToInt(ComponenteCatalogo::contarProdutos).sum();
    }

    @Override
    public String exibir(int nivel) {
        StringBuilder saida = new StringBuilder(cabecalho(nivel));
        for (ComponenteCatalogo filho : filhos) {
            saida.append(filho.exibir(nivel + 1));
        }
        return saida.toString();
    }

    public CategoriaCatalogo obterOuCriarSubcategoria(String nomeDaSubcategoria) {
        return subcategoriaChamada(nomeDaSubcategoria).orElseGet(() -> {
            CategoriaCatalogo nova = new CategoriaCatalogo(nomeDaSubcategoria);
            adicionar(nova);
            return nova;
        });
    }

    private Optional<CategoriaCatalogo> subcategoriaChamada(String nomeProcurado) {
        return filhos.stream()
                .filter(CategoriaCatalogo.class::isInstance)
                .map(CategoriaCatalogo.class::cast)
                .filter(subcategoria -> subcategoria.getNome().equals(nomeProcurado))
                .findFirst();
    }

    private String cabecalho(int nivel) {
        int total = contarProdutos();
        String unidade = total == 1 ? " produto" : " produtos";
        return Recuo.ate(nivel) + "+ " + nome + " (" + total + unidade + ")"
                + System.lineSeparator();
    }
}
