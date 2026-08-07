package com.mps.shared.facade;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.mps.shared.command.RegistroComando;
import com.mps.shared.factory.HibernateRepositoryFactory;
import com.mps.shared.factory.InMemoryRepositoryFactory;
import com.mps.shared.factory.RepositoryFactory;

public final class FacadeSingletonController {

    private static FacadeSingletonController instance;

    private FacadeSingletonController() {
    }

    public static synchronized FacadeSingletonController getInstance(boolean usarBancoDeDados) {
        if (instance == null) {
            RepositoryFactory factory = usarBancoDeDados
                    ? new HibernateRepositoryFactory()
                    : new InMemoryRepositoryFactory();
            UserFacade.getInstance(factory);
            ProdutoFacade.getInstance(factory);
            AnuncioFacade.getInstance(factory);
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

    public List<RegistroComando> historicoDeComandos() {
        return Stream.of(
                        UserFacade.getInstance().getInvoker(),
                        ProdutoFacade.getInstance().getInvoker(),
                        AnuncioFacade.getInstance().getInvoker())
                .flatMap(executor -> executor.getHistorico().stream())
                .sorted(Comparator.comparing(RegistroComando::momento))
                .toList();
    }
}
