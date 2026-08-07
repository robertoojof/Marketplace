package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.application.AnuncioCaretaker;
import com.mps.anuncios.application.AnuncioService;
import com.mps.anuncios.application.command.AdicionarAnuncioCommand;
import com.mps.anuncios.application.command.AtualizarAnuncioCommand;
import com.mps.anuncios.application.command.DesativarAnunciosDoVendedorCommand;
import com.mps.anuncios.application.command.DesfazerAtualizacaoAnuncioCommand;
import com.mps.anuncios.application.command.ReativarAnuncioCommand;
import com.mps.anuncios.application.command.RemoverAnuncioCommand;
import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.command.CommandInvoker;
import com.mps.shared.factory.RepositoryFactory;

public final class AnuncioFacade {

    private static AnuncioFacade instance;

    private final AnuncioController anuncioController;
    private final AnuncioCaretaker caretaker = new AnuncioCaretaker();
    private final CommandInvoker invoker = new CommandInvoker();

    private AnuncioFacade(RepositoryFactory factory) {
        IAnuncioRepository anuncioRepository = factory.criarAnuncioRepository();
        this.anuncioController = new AnuncioController(new AnuncioService(anuncioRepository,
                ProdutoFacade.getInstance(factory).getRepository(),
                UserFacade.getInstance(factory).getRepository()));
    }

    public static synchronized AnuncioFacade getInstance(RepositoryFactory factory) {
        if (instance == null) {
            instance = new AnuncioFacade(factory);
        }
        return instance;
    }

    public static synchronized AnuncioFacade getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AnuncioFacade ainda não foi inicializada");
        }
        return instance;
    }

    static synchronized void reset() {
        instance = null;
    }

    CommandInvoker getInvoker() {
        return invoker;
    }

    public void adicionarAnuncio(Anuncio anuncio) {
        invoker.executar(new AdicionarAnuncioCommand(anuncioController, anuncio));
    }

    public List<Anuncio> listarAnuncios() {
        return anuncioController.listarAnuncios();
    }

    public Optional<Anuncio> buscarAnuncioPorId(UUID id) {
        return anuncioController.buscarAnuncioPorId(id);
    }

    public List<Anuncio> buscarAnunciosPorVendedor(UUID vendedorId) {
        return anuncioController.buscarAnunciosPorVendedor(vendedorId);
    }

    public Anuncio atualizarAnuncio(Anuncio anuncio) {
        return invoker.executar(new AtualizarAnuncioCommand(anuncioController, caretaker, anuncio));
    }

    public void removerAnuncio(UUID id) {
        invoker.executar(new RemoverAnuncioCommand(anuncioController, id));
    }

    public void reativarAnuncio(UUID id) {
        invoker.executar(new ReativarAnuncioCommand(anuncioController, id));
    }

    public void desativarAnunciosDoVendedor(UUID vendedorId) {
        invoker.executar(new DesativarAnunciosDoVendedorCommand(anuncioController, vendedorId));
    }

    public Anuncio desfazerUltimaAtualizacao() {
        return invoker.executar(new DesfazerAtualizacaoAnuncioCommand(anuncioController, caretaker));
    }

    public boolean possuiAtualizacaoParaDesfazer() {
        return caretaker.possuiEstadoSalvo();
    }

    public int contarAnuncios() {
        return (int) listarAnuncios().stream().filter(Anuncio::isAtivo).count();
    }
}
