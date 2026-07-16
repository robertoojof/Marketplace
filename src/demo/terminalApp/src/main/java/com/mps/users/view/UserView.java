package com.mps.users.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import com.mps.shared.exception.AutorizacaoException;
import com.mps.shared.exception.RepositorioException;
import com.mps.shared.facade.FacadeSingletonController;
import com.mps.shared.facade.UserFacade;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;
import com.mps.users.domain.exception.ValidacaoUsuarioException;

public class UserView {

    private final Scanner scanner;
    private final UserFacade userFacade;
    private final FacadeSingletonController facade;

    public UserView(Scanner scanner, UserFacade userFacade, FacadeSingletonController facade) {
        this.scanner = scanner;
        this.userFacade = userFacade;
        this.facade = facade;
    }

    public void userMenu() {
        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Gerenciar Usuários");
                System.out.println("=============================");
                System.out.println("  1. Adicionar usuário");
                System.out.println("  2. Listar todos os usuários");
                System.out.println("  3. Buscar usuário por ID");
                System.out.println("  4. Atualizar usuário");
                System.out.println("  5. Remover usuário");
                System.out.println("  6. Reativar usuário");
                System.out.println("  7. Voltar");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> adicionarUsuario();
                    case 2 -> listarUsuarios();
                    case 3 -> buscarUsuarioPorId();
                    case 4 -> atualizarUsuario();
                    case 5 -> removerUsuario();
                    case 6 -> reativarUsuario();
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

        User user = new User(UUID.randomUUID(), login, cpf, nome, email, senha, Role.USER, true);

        try {
            userFacade.adicionarUsuario(user);
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
            List<User> usuarios = userFacade.listarUsuarios();
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
                return;
            }
            System.out.println("\n--- Lista de Usuários ---");
            for (User u : usuarios) {
                imprimirUsuario(u);
            }
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar usuários: " + e.getMessage());
        }
    }

    private void buscarUsuarioPorId() {
        UUID id = lerUuid();
        if (id == null) return;

        try {
            Optional<User> usuario = userFacade.buscarUsuarioPorId(id);
            if (usuario.isEmpty()) {
                System.out.println("Usuário não encontrado.");
                return;
            }
            System.out.println("\n--- Usuário encontrado ---");
            imprimirUsuario(usuario.get());
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar usuário: " + e.getMessage());
        }
    }

    private void atualizarUsuario() {
        UUID id = lerUuid();
        if (id == null) return;

        try {
            Optional<User> existente = userFacade.buscarUsuarioPorId(id);
            if (existente.isEmpty()) {
                System.out.println("Usuário não encontrado.");
                return;
            }
            User usuario = existente.get();

            System.out.printf("Login [%s]: ", usuario.getLogin());
            String login = scanner.nextLine();
            if (!login.isBlank()) usuario.setLogin(login);

            System.out.printf("Nome [%s]: ", usuario.getName());
            String nome = scanner.nextLine();
            if (!nome.isBlank()) usuario.setName(nome);

            System.out.printf("CPF [%s]: ", usuario.getCpf());
            String cpf = scanner.nextLine();
            if (!cpf.isBlank()) usuario.setCpf(cpf);

            System.out.printf("Email [%s]: ", usuario.getEmail());
            String email = scanner.nextLine();
            if (!email.isBlank()) usuario.setEmail(email);

            System.out.print("Senha (deixe em branco para manter a atual): ");
            String senha = scanner.nextLine();
            if (!senha.isBlank()) usuario.setPassword(senha);

            userFacade.atualizarUsuario(usuario);
            System.out.println("Usuário atualizado com sucesso!");
        } catch (ValidacaoUsuarioException e) {
            System.out.println("\nErros de validação:");
            e.getErros().forEach(erro -> System.out.println("  - " + erro));
        } catch (RepositorioException e) {
            System.out.println("\nErro ao atualizar usuário: " + e.getMessage());
        }
    }

    private void removerUsuario() {
        UUID id = lerUuid();
        if (id == null) return;

        System.out.print("Confirma a remoção do usuário? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Remoção cancelada.");
            return;
        }

        try {
            facade.removerUsuario(id);
            System.out.println("Usuário removido com sucesso!");
        } catch (RepositorioException e) {
            System.out.println("\nErro ao remover usuário: " + e.getMessage());
        }
    }

    private void reativarUsuario() {
        UUID id = lerUuid();
        if (id == null) return;

        System.out.print("Seu login (autorização): ");
        String loginAutorizador = scanner.nextLine();
        System.out.print("Sua senha: ");
        String senhaAutorizador = scanner.nextLine();

        try {
            userFacade.reativarUsuario(id, loginAutorizador, senhaAutorizador);
            System.out.println("Usuário reativado com sucesso!");
        } catch (AutorizacaoException e) {
            System.out.println("\nErro de autorização: " + e.getMessage());
        } catch (RepositorioException e) {
            System.out.println("\nErro ao reativar usuário: " + e.getMessage());
        }
    }

    private UUID lerUuid() {
        System.out.print("ID do usuário: ");
        String entrada = scanner.nextLine();
        try {
            return UUID.fromString(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }

    private void imprimirUsuario(User u) {
        System.out.printf("ID: %s | Login: %s | Nome: %s | CPF: %s | Email: %s | Perfil: %s | Status: %s%n",
                u.getId(), u.getLogin(), u.getName(), u.getCpf(), u.getEmail(), u.getRole(),
                u.isAtivo() ? "Ativo" : "Inativo");
    }
}
