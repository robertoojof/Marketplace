package com.mps.shared.facade;

import java.util.UUID;

public final class FacadeSingletonController {

    private static FacadeSingletonController instance;

    private FacadeSingletonController() {
    }

    public static synchronized FacadeSingletonController getInstance(boolean usarBancoDeDados) {
        if (instance == null) {
            UserFacade.getInstance(usarBancoDeDados);
            ProdutoFacade.getInstance(usarBancoDeDados);
            AnuncioFacade.getInstance(usarBancoDeDados);
            instance = new FacadeSingletonController();
        }
        return instance;
    }

    static synchronized void reset() {
        instance = null;
        UserFacade.reset();
        ProdutoFacade.reset();
        AnuncioFacade.reset();
    }

    public void removerUsuario(UUID id) {
        UserFacade.getInstance().removerUsuario(id);
        AnuncioFacade.getInstance().desativarAnunciosDoVendedor(id);
    }

    public int contarUsuarios() {
        return UserFacade.getInstance().contarUsuarios();
    }

    public int contarProdutos() {
        return ProdutoFacade.getInstance().contarProdutos();
    }

    public int contarAnuncios() {
        return AnuncioFacade.getInstance().contarAnuncios();
    }

    public int contarEntidadesCadastradas() {
        return contarUsuarios() + contarProdutos() + contarAnuncios();
    }
}
