package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.produtos.application.ProdutoService;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.domain.Produto;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.shared.factory.RepositoryFactory;

public final class ProdutoFacade {

    private static ProdutoFacade instance;

    private final IProdutoRepository produtoRepository;
    private final ProdutoController produtoController;

    private ProdutoFacade(RepositoryFactory factory) {
        this.produtoRepository = factory.criarProdutoRepository();
        this.produtoController = new ProdutoController(new ProdutoService(produtoRepository));
    }

    public static synchronized ProdutoFacade getInstance(RepositoryFactory factory) {
        if (instance == null) {
            instance = new ProdutoFacade(factory);
        }
        return instance;
    }

    public static synchronized ProdutoFacade getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ProdutoFacade ainda não foi inicializada");
        }
        return instance;
    }

    IProdutoRepository getRepository() {
        return produtoRepository;
    }

    static synchronized void reset() {
        instance = null;
    }

    public void adicionarProduto(Produto produto) {
        produtoController.adicionarProduto(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoController.listarProdutos();
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        return produtoController.buscarProdutoPorId(id);
    }

    public Produto atualizarProduto(Produto produto) {
        return produtoController.atualizarProduto(produto);
    }

    public void removerProduto(UUID id) {
        produtoController.removerProduto(id);
    }

    public void reativarProduto(UUID id) {
        produtoController.reativarProduto(id);
    }

    public int contarProdutos() {
        return (int) listarProdutos().stream().filter(Produto::isAtivo).count();
    }
}
