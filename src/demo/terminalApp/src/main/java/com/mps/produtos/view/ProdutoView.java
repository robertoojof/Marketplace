package com.mps.produtos.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import com.mps.produtos.domain.Produto;
import com.mps.produtos.domain.exception.ValidacaoProdutoException;
import com.mps.produtos.presentation.controller.ProdutoController;
import com.mps.shared.exception.RepositorioException;

public class ProdutoView {

    private final Scanner scanner;
    private final ProdutoController produtoController;

    public ProdutoView(Scanner scanner, ProdutoController produtoController) {
        this.scanner = scanner;
        this.produtoController = produtoController;
    }

    public void produtoMenu() {
        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Gerenciar Produtos (Catálogo)");
                System.out.println("=============================");
                System.out.println("  1. Adicionar produto");
                System.out.println("  2. Listar todos os produtos");
                System.out.println("  3. Buscar produto por ID");
                System.out.println("  4. Atualizar produto");
                System.out.println("  5. Remover produto");
                System.out.println("  6. Reativar produto");
                System.out.println("  7. Voltar");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> adicionarProduto();
                    case 2 -> listarProdutos();
                    case 3 -> buscarProdutoPorId();
                    case 4 -> atualizarProduto();
                    case 5 -> removerProduto();
                    case 6 -> reativarProduto();
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

    private void adicionarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Produto produto = new Produto(UUID.randomUUID(), nome, descricao, true);

        try {
            produtoController.adicionarProduto(produto);
            System.out.println("Produto adicionado com sucesso!");
        } catch (ValidacaoProdutoException e) {
            System.out.println("\nErros de validação:");
            e.getErros().forEach(erro -> System.out.println("  - " + erro));
        } catch (RepositorioException e) {
            System.out.println("\nErro ao salvar produto: " + e.getMessage());
        }
    }

    private void listarProdutos() {
        try {
            List<Produto> produtos = produtoController.listarProdutos();
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }
            System.out.println("\n--- Lista de Produtos ---");
            for (Produto p : produtos) {
                imprimirProduto(p);
            }
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar produtos: " + e.getMessage());
        }
    }

    private void buscarProdutoPorId() {
        UUID id = lerUuid();
        if (id == null) return;

        try {
            Optional<Produto> produto = produtoController.buscarProdutoPorId(id);
            if (produto.isEmpty()) {
                System.out.println("Produto não encontrado.");
                return;
            }
            System.out.println("\n--- Produto encontrado ---");
            imprimirProduto(produto.get());
        } catch (RepositorioException e) {
            System.out.println("\nErro ao buscar produto: " + e.getMessage());
        }
    }

    private void atualizarProduto() {
        UUID id = lerUuid();
        if (id == null) return;

        try {
            Optional<Produto> existente = produtoController.buscarProdutoPorId(id);
            if (existente.isEmpty()) {
                System.out.println("Produto não encontrado.");
                return;
            }
            Produto produto = existente.get();

            System.out.printf("Nome [%s]: ", produto.getNome());
            String nome = scanner.nextLine();
            if (!nome.isBlank()) produto.setNome(nome);

            System.out.printf("Descrição [%s]: ", produto.getDescricao());
            String descricao = scanner.nextLine();
            if (!descricao.isBlank()) produto.setDescricao(descricao);

            produtoController.atualizarProduto(produto);
            System.out.println("Produto atualizado com sucesso!");
        } catch (ValidacaoProdutoException e) {
            System.out.println("\nErros de validação:");
            e.getErros().forEach(erro -> System.out.println("  - " + erro));
        } catch (RepositorioException e) {
            System.out.println("\nErro ao atualizar produto: " + e.getMessage());
        }
    }

    private void removerProduto() {
        UUID id = lerUuid();
        if (id == null) return;

        System.out.print("Confirma a remoção do produto? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Remoção cancelada.");
            return;
        }

        try {
            produtoController.removerProduto(id);
            System.out.println("Produto removido com sucesso!");
        } catch (RepositorioException e) {
            System.out.println("\nErro ao remover produto: " + e.getMessage());
        }
    }

    private void reativarProduto() {
        UUID id = lerUuid();
        if (id == null) return;

        try {
            produtoController.reativarProduto(id);
            System.out.println("Produto reativado com sucesso!");
        } catch (RepositorioException e) {
            System.out.println("\nErro ao reativar produto: " + e.getMessage());
        }
    }

    private UUID lerUuid() {
        System.out.print("ID do produto: ");
        String entrada = scanner.nextLine();
        try {
            return UUID.fromString(entrada);
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }

    private void imprimirProduto(Produto p) {
        System.out.printf("ID: %s | Nome: %s | Descrição: %s | Status: %s%n",
                p.getId(), p.getNome(), p.getDescricao(), p.isAtivo() ? "Ativo" : "Inativo");
    }
}
