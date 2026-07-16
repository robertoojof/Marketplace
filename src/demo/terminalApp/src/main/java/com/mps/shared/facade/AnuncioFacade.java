package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.application.AnuncioService;
import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.shared.factory.RepositoryFactory;

public final class AnuncioFacade {

    private static AnuncioFacade instance;

    private final AnuncioController anuncioController;

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

    public void adicionarAnuncio(Anuncio anuncio) {
        anuncioController.adicionarAnuncio(anuncio);
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
        return anuncioController.atualizarAnuncio(anuncio);
    }

    public void removerAnuncio(UUID id) {
        anuncioController.removerAnuncio(id);
    }

    public void reativarAnuncio(UUID id) {
        anuncioController.reativarAnuncio(id);
    }

    public void desativarAnunciosDoVendedor(UUID vendedorId) {
        anuncioController.desativarAnunciosDoVendedor(vendedorId);
    }

    public int contarAnuncios() {
        return (int) listarAnuncios().stream().filter(Anuncio::isAtivo).count();
    }
}
