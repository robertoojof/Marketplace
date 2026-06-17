package com.mps;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.mps.users.UsersModule;
import com.mps.users.view.UserView;

public class TerminalApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean usarBancoDeDados = selecionarArmazenamento(scanner);
        UserView userView = UsersModule.create(scanner, usarBancoDeDados);

        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Oministore (Terminal edition)   ");
                System.out.println("=============================");
                System.out.println("Escolha uma opção:");
                System.out.println("  1. Gerenciar Usuários");
                System.out.println("  7. Sair");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> userView.userMenu();
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
