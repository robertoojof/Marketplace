package com.mps.anuncios;

import java.util.Scanner;

import com.mps.anuncios.application.AnuncioService;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.infrastructure.HibernateAnuncioRepository;
import com.mps.anuncios.infrastructure.InMemoryAnuncioRepository;
import com.mps.anuncios.presentation.controller.AnuncioController;
import com.mps.anuncios.view.AnuncioView;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.users.domain.IUserRepository;
import com.mps.users.presentation.controller.UserController;

public class AnunciosModule {

    public static AnuncioView create(Scanner scanner, boolean usarBancoDeDados, IProdutoRepository produtoRepository,
            IUserRepository userRepository, ProdutoController produtoController, UserController userController) {
        IAnuncioRepository repository = usarBancoDeDados
                ? new HibernateAnuncioRepository()
                : new InMemoryAnuncioRepository();
        AnuncioService service = new AnuncioService(repository, produtoRepository, userRepository);
        AnuncioController controller = new AnuncioController(service);
        return new AnuncioView(scanner, controller, produtoController, userController);
    }
}
