package com.mps.users.view;

import java.util.Scanner;

public class UserView {
  private final Scanner scanner;

  public UserView(Scanner scanner) {
    this.scanner = scanner;
  }

  public void userMenu() {
    while (true) {
      System.out.println("Escolha uma opção:");
      System.out.println("1. Adicionar usuário");
      System.out.println("2. Listar todos os usuários");

      int in = scanner.nextInt();
      scanner.nextLine();

      switch (in) {
        case 1 -> registerUser();
        case 2 -> listUsers();
        case 7 -> {
          System.out.println("Saindo...");
          return;
        }
        default -> System.out.println("Opção inválida. Tente novamente.");
      }
    }
  }

  private void registerUser() {
    System.out.println("Registrar usuário...");
    // Lógica para registrar um usuário
  }

  private void listUsers() {
    System.out.println("Listar usuários...");
    // Lógica para listar usuários
  }
}
