package com.mps.users.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.mps.shared.exception.RepositorioException;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;
import com.mps.users.domain.exception.ValidacaoUsuarioException;
import com.mps.users.presentation.controller.UserController;

public class UserView {

    private final Scanner scanner;
    private final UserController userController;

    public UserView(Scanner scanner, UserController userController) {
        this.scanner = scanner;
        this.userController = userController;
    }

    public void userMenu() {
        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Gerenciar Usuários");
                System.out.println("=============================");
                System.out.println("  1. Adicionar usuário");
                System.out.println("  2. Listar todos os usuários");
                System.out.println("  7. Voltar");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> adicionarUsuario();
                    case 2 -> listarUsuarios();
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

    private void adicionarUsuario() {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        User user = new User(UUID.randomUUID(), login, cpf, nome, email, senha, Role.USER);

        try {
            userController.adicionarUsuario(user);
            System.out.println("Usuário adicionado com sucesso!");
        } catch (ValidacaoUsuarioException e) {
            System.out.println("\nErros de validação:");
            e.getErros().forEach(erro -> System.out.println("  - " + erro));
        } catch (RepositorioException e) {
            System.out.println("\nErro ao salvar usuário: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        try {
            List<User> usuarios = userController.listarUsuarios();
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
                return;
            }
            System.out.println("\n--- Lista de Usuários ---");
            for (User u : usuarios) {
                System.out.printf("ID: %s | Login: %s | Nome: %s | CPF: %s | Email: %s | Perfil: %s%n",
                        u.getId(), u.getLogin(), u.getName(), u.getCpf(), u.getEmail(), u.getRole());
            }
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar usuários: " + e.getMessage());
        }
    }
}
