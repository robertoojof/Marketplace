package com.mps.produtos.presentation.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.produtos.application.ProdutoService;
import com.mps.produtos.domain.Produto;

public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void adicionarProduto(Produto produto) {
        produtoService.adicionarProduto(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        return produtoService.buscarProdutoPorId(id);
    }

    public Produto atualizarProduto(Produto produto) {
        return produtoService.atualizarProduto(produto);
    }

    public void removerProduto(UUID id) {
        produtoService.removerProduto(id);
    }

    public void reativarProduto(UUID id) {
        produtoService.reativarProduto(id);
    }
}
