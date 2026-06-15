package com.mps;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.mps.users.UsersModule;
import com.mps.users.view.UserView;

public class TerminalApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserView userView = UsersModule.create(scanner);

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
}
