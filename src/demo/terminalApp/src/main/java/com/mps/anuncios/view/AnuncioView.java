package com.mps.anuncios.view;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.exception.ValidacaoAnuncioException;
import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.RepositorioException;
import com.mps.shared.facade.AnuncioFacade;
import com.mps.shared.facade.ProdutoFacade;
import com.mps.shared.facade.UserFacade;
import com.mps.users.domain.User;

public class AnuncioView {

    private final Scanner scanner;
    private final AnuncioFacade anuncioFacade;
    private final ProdutoFacade produtoFacade;
    private final UserFacade userFacade;

    public AnuncioView(Scanner scanner, AnuncioFacade anuncioFacade, ProdutoFacade produtoFacade,
            UserFacade userFacade) {
        this.scanner = scanner;
        this.anuncioFacade = anuncioFacade;
        this.produtoFacade = produtoFacade;
        this.userFacade = userFacade;
    }

    public void anuncioMenu() {
        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Gerenciar Anúncios");
                System.out.println("=============================");
                System.out.println("  1. Criar anúncio");
                System.out.println("  2. Listar todos os anúncios");
                System.out.println("  3. Buscar anúncio por ID");
                System.out.println("  4. Atualizar anúncio");
                System.out.println("  5. Remover anúncio");
                System.out.println("  6. Reativar anúncio");
                System.out.println("  7. Voltar");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> criarAnuncio();
                    case 2 -> listarAnuncios();
                    case 3 -> buscarAnuncioPorId();
                    case 4 -> atualizarAnuncio();
                    case 5 -> removerAnuncio();
                    case 6 -> reativarAnuncio();
                    case 7 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }

    private void criarAnuncio() {
        Produto produto = lerProdutoPorId();
        if (produto == null) return;

        User vendedor = lerVendedorPorLogin();
        if (vendedor == null) return;

        BigDecimal preco = lerPreco();
        if (preco == null) return;

        Integer quantidade = lerQuantidade();
        if (quantidade == null) return;

        Anuncio anuncio = new Anuncio(UUID.randomUUID(), produto, vendedor, preco, quantidade, true);

        try {
            anuncioFacade.adicionarAnuncio(anuncio);
            System.out.println("Anúncio criado com sucesso!");
        } catch (ValidacaoAnuncioException e) {
            System.out.println("\nErros de validação:");
            e.getErros().forEach(erro -> System.out.println("  - " + erro));
        } catch (RepositorioException e) {
            System.out.println("\nErro ao salvar anúncio: " + e.getMessage());
        }
    }

    private void listarAnuncios() {
        try {
            List<Anuncio> anuncios = anuncioFacade.listarAnuncios();
            if (anuncios.isEmpty()) {
                System.out.println("Nenhum anúncio cadastrado.");
                return;
            }
            System.out.println("\n--- Lista de Anúncios ---");
            for (Anuncio a : anuncios) {
                imprimirAnuncio(a);
            }
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar anúncios: " + e.getMessage());
        }
    }

    private void buscarAnuncioPorId() {
        UUID id = lerUuid("ID do anúncio: ");
        if (id == null) return;

        try {
            Optional<Anuncio> anuncio = anuncioFacade.buscarAnuncioPorId(id);
            if (anuncio.isEmpty()) {
                System.out.println("Anúncio não encontrado.");
                return;
            }
            System.out.println("\n--- Anúncio encontrado ---");
            imprimirAnuncio(anuncio.get());
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar anúncio: " + e.getMessage());
        }
    }

    private void atualizarAnuncio() {
        UUID id = lerUuid("ID do anúncio: ");
        if (id == null) return;

        try {
            Optional<Anuncio> existente = anuncioFacade.buscarAnuncioPorId(id);
            if (existente.isEmpty()) {
                System.out.println("Anúncio não encontrado.");
                return;
            }
            Anuncio anuncio = existente.get();

            System.out.printf("Preço [%s] (deixe em branco para manter): ", anuncio.getPreco());
            String precoEntrada = scanner.nextLine();
            if (!precoEntrada.isBlank()) {
                try {
                    anuncio.setPreco(new BigDecimal(precoEntrada));
                } catch (NumberFormatException e) {
                    System.out.println("Preço inválido, mantendo o valor atual.");
                }
            }

            System.out.printf("Quantidade em estoque [%d] (deixe em branco para manter): ",
                    anuncio.getQuantidadeEmEstoque());
            String quantidadeEntrada = scanner.nextLine();
            if (!quantidadeEntrada.isBlank()) {
                try {
                    anuncio.setQuantidadeEmEstoque(Integer.parseInt(quantidadeEntrada));
                } catch (NumberFormatException e) {
                    System.out.println("Quantidade inválida, mantendo o valor atual.");
                }
            }

            anuncioFacade.atualizarAnuncio(anuncio);
            System.out.println("Anúncio atualizado com sucesso!");
        } catch (ValidacaoAnuncioException e) {
            System.out.println("\nErros de validação:");
            e.getErros().forEach(erro -> System.out.println("  - " + erro));
        } catch (RepositorioException e) {
            System.out.println("\nErro ao atualizar anúncio: " + e.getMessage());
        }
    }

    private void removerAnuncio() {
        UUID id = lerUuid("ID do anúncio: ");
        if (id == null) return;

        System.out.print("Confirma a remoção do anúncio? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Remoção cancelada.");
            return;
        }

        try {
            anuncioFacade.removerAnuncio(id);
            System.out.println("Anúncio removido com sucesso!");
        } catch (RepositorioException e) {
            System.out.println("\nErro ao remover anúncio: " + e.getMessage());
        }
    }

    private void reativarAnuncio() {
        UUID id = lerUuid("ID do anúncio: ");
        if (id == null) return;

        try {
            anuncioFacade.reativarAnuncio(id);
            System.out.println("Anúncio reativado com sucesso!");
        } catch (RepositorioException e) {
            System.out.println("\nErro ao reativar anúncio: " + e.getMessage());
        }
    }

    private Produto lerProdutoPorId() {
        UUID id = lerUuid("ID do produto (catálogo): ");
        if (id == null) return null;

        Optional<Produto> produto = produtoFacade.buscarProdutoPorId(id);
        if (produto.isEmpty()) {
            System.out.println("Produto não encontrado no catálogo.");
            return null;
        }
        return produto.get();
    }

    private User lerVendedorPorLogin() {
        System.out.print("Login do vendedor: ");
        String login = scanner.nextLine();

        Optional<User> vendedor = userFacade.buscarUsuarioPorLogin(login);
        if (vendedor.isEmpty()) {
            System.out.println("Vendedor não encontrado.");
            return null;
        }
        return vendedor.get();
    }

    private BigDecimal lerPreco() {
        System.out.print("Preço: ");
        String entrada = scanner.nextLine();
        try {
            return new BigDecimal(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido.");
            return null;
        }
    }

    private Integer lerQuantidade() {
        System.out.print("Quantidade em estoque: ");
        String entrada = scanner.nextLine();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida.");
            return null;
        }
    }

    private UUID lerUuid(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine();
        try {
            return UUID.fromString(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }

    private void imprimirAnuncio(Anuncio a) {
        System.out.printf("ID: %s | Produto: %s | Vendedor: %s | Preço: %s | Estoque: %d | Status: %s%n",
                a.getId(), a.getProduto().getNome(), a.getVendedor().getLogin(), a.getPreco(),
                a.getQuantidadeEmEstoque(), a.isAtivo() ? "Ativo" : "Inativo");
    }
}
