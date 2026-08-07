package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.produtos.application.MontadorDeCatalogo;
import com.mps.produtos.application.ProdutoService;
import com.mps.produtos.application.command.AdicionarProdutoCommand;
import com.mps.produtos.application.command.AtualizarProdutoCommand;
import com.mps.produtos.application.command.ReativarProdutoCommand;
import com.mps.produtos.application.command.RemoverProdutoCommand;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.domain.Produto;
import com.mps.produtos.domain.catalogo.CategoriaCatalogo;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.shared.command.CommandInvoker;
import com.mps.shared.factory.RepositoryFactory;

public final class ProdutoFacade {

    private static final String NOME_DO_CATALOGO = "Catálogo OmniMart";

    private static ProdutoFacade instance;

    private final IProdutoRepository produtoRepository;
    private final ProdutoController produtoController;
    private final CommandInvoker invoker = new CommandInvoker();
    private final MontadorDeCatalogo montadorDeCatalogo = new MontadorDeCatalogo();

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

    CommandInvoker getInvoker() {
        return invoker;
    }

    static synchronized void reset() {
        instance = null;
    }

    public void adicionarProduto(Produto produto) {
        invoker.executar(new AdicionarProdutoCommand(produtoController, produto));
    }

    public List<Produto> listarProdutos() {
        return produtoController.listarProdutos();
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        return produtoController.buscarProdutoPorId(id);
    }

    public Produto atualizarProduto(Produto produto) {
        return invoker.executar(new AtualizarProdutoCommand(produtoController, produto));
    }

    public void removerProduto(UUID id) {
        invoker.executar(new RemoverProdutoCommand(produtoController, id));
    }

    public void reativarProduto(UUID id) {
        invoker.executar(new ReativarProdutoCommand(produtoController, id));
    }

    public CategoriaCatalogo montarCatalogo() {
        return montadorDeCatalogo.montar(NOME_DO_CATALOGO, listarProdutos());
    }

    public int contarProdutos() {
        return (int) listarProdutos().stream().filter(Produto::isAtivo).count();
    }
}
