package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.application.AnuncioService;
import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.infrastructure.HibernateAnuncioRepository;
import com.mps.anuncios.infrastructure.InMemoryAnuncioRepository;
import com.mps.anuncios.presentation.controller.AnuncioController;

public final class AnuncioFacade {

    private static AnuncioFacade instance;

    private final AnuncioController anuncioController;

    private AnuncioFacade(boolean usarBancoDeDados) {
        IAnuncioRepository anuncioRepository = usarBancoDeDados
                ? new HibernateAnuncioRepository()
                : new InMemoryAnuncioRepository();
        this.anuncioController = new AnuncioController(new AnuncioService(anuncioRepository,
                ProdutoFacade.getInstance(usarBancoDeDados).getRepository(),
                UserFacade.getInstance(usarBancoDeDados).getRepository()));
    }

    public static synchronized AnuncioFacade getInstance(boolean usarBancoDeDados) {
        if (instance == null) {
            instance = new AnuncioFacade(usarBancoDeDados);
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
