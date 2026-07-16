package com.mps;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.mps.anuncios.view.AnuncioView;
import com.mps.produtos.view.ProdutoView;
import com.mps.relatorios.view.RelatorioAcessoView;
import com.mps.shared.facade.AnuncioFacade;
import com.mps.shared.facade.FacadeSingletonController;
import com.mps.shared.facade.ProdutoFacade;
import com.mps.shared.facade.UserFacade;
import com.mps.users.view.UserView;

public class TerminalApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean usarBancoDeDados = selecionarArmazenamento(scanner);

        FacadeSingletonController facade = FacadeSingletonController.getInstance(usarBancoDeDados);
        UserFacade userFacade = UserFacade.getInstance();
        ProdutoFacade produtoFacade = ProdutoFacade.getInstance();
        AnuncioFacade anuncioFacade = AnuncioFacade.getInstance();

        UserView userView = new UserView(scanner, userFacade, facade);
        ProdutoView produtoView = new ProdutoView(scanner, produtoFacade);
        AnuncioView anuncioView = new AnuncioView(scanner, anuncioFacade, produtoFacade, userFacade);
        RelatorioAcessoView relatorioAcessoView = new RelatorioAcessoView(scanner, userFacade);

        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Oministore (Terminal edition)   ");
                System.out.println("=============================");
                System.out.println("Escolha uma opção:");
                System.out.println("  1. Gerenciar Usuários");
                System.out.println("  2. Gerenciar Produtos (Catálogo)");
                System.out.println("  3. Gerenciar Anúncios");
                System.out.println("  4. Total de entidades cadastradas");
                System.out.println("  5. Relatórios de acesso");
                System.out.println("  7. Sair");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> userView.userMenu();
                    case 2 -> produtoView.produtoMenu();
                    case 3 -> anuncioView.anuncioMenu();
                    case 4 -> exibirTotalEntidades(facade);
                    case 5 -> relatorioAcessoView.relatorioMenu();
                    case 7 -> {
                        System.out.println("Encerrando o programa...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }

    private static void exibirTotalEntidades(FacadeSingletonController facade) {
        System.out.println("\n=============================");
        System.out.println("   Total de entidades cadastradas");
        System.out.println("=============================");
        System.out.println("Usuários ativos: " + facade.contarUsuarios());
        System.out.println("Produtos ativos: " + facade.contarProdutos());
        System.out.println("Anúncios ativos: " + facade.contarAnuncios());
        System.out.println("Total: " + facade.contarEntidadesCadastradas());
    }

    private static boolean selecionarArmazenamento(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Tipo de Armazenamento");
                System.out.println("=============================");
                System.out.println("  1. Memória RAM (temporário)");
                System.out.println("  2. Banco de Dados H2 (persistente)");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) return false;
                if (opcao == 2) return true;

                System.out.println("Opção inválida. Escolha 1 ou 2.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }
}
