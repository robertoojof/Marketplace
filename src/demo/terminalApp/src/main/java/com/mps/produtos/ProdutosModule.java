package com.mps.produtos;

import java.util.Scanner;

import com.mps.produtos.application.ProdutoService;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.infrastructure.HibernateProdutoRepository;
import com.mps.produtos.infrastructure.InMemoryProdutoRepository;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.produtos.view.ProdutoView;

public class ProdutosModule {

    public record Bundle(IProdutoRepository repository, ProdutoController controller, ProdutoView view) {
    }

    public static Bundle create(Scanner scanner, boolean usarBancoDeDados) {
        IProdutoRepository repository = usarBancoDeDados
                ? new HibernateProdutoRepository()
                : new InMemoryProdutoRepository();
        ProdutoService service = new ProdutoService(repository);
        ProdutoController controller = new ProdutoController(service);
        ProdutoView view = new ProdutoView(scanner, controller);
        return new Bundle(repository, controller, view);
    }
}
