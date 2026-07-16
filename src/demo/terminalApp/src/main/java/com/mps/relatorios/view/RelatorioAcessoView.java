package com.mps.relatorios.view;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.mps.acessos.domain.AcessoLog;
import com.mps.relatorios.RelatorioAcessoTemplate;
import com.mps.relatorios.RelatorioException;
import com.mps.relatorios.RelatorioHtmlAcesso;
import com.mps.relatorios.RelatorioPdfAcesso;
import com.mps.relatorios.RelatorioTextoAcesso;
import com.mps.shared.facade.UserFacade;
import com.mps.users.domain.User;

public class RelatorioAcessoView {

    private final Scanner scanner;
    private final UserFacade userFacade;

    public RelatorioAcessoView(Scanner scanner, UserFacade userFacade) {
        this.scanner = scanner;
        this.userFacade = userFacade;
    }

    public void relatorioMenu() {
        while (true) {
            try {
                System.out.println("\n=============================");
                System.out.println("   Relatórios de Acesso");
                System.out.println("=============================");
                System.out.println("  1. Gerar relatório em Texto");
                System.out.println("  2. Gerar relatório em HTML");
                System.out.println("  3. Gerar relatório em PDF");
                System.out.println("  7. Voltar");
                System.out.println("=============================");
                System.out.print("Digite a sua escolha: ");

                int in = scanner.nextInt();
                scanner.nextLine();

                switch (in) {
                    case 1 -> gerarRelatorio(new RelatorioTextoAcesso());
                    case 2 -> gerarRelatorio(new RelatorioHtmlAcesso());
                    case 3 -> gerarRelatorio(new RelatorioPdfAcesso());
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

    private void gerarRelatorio(RelatorioAcessoTemplate<?> relatorio) {
        try {
            List<AcessoLog> logs = userFacade.listarAcessos();
            List<User> usuarios = userFacade.listarUsuarios();
            byte[] conteudo = relatorio.gerar(logs, usuarios);

            if (".txt".equals(relatorio.extensaoArquivo())) {
                new PrintStream(System.out, true, StandardCharsets.UTF_8).println(new String(conteudo, StandardCharsets.UTF_8));
                return;
            }

            Path arquivo = Path.of("relatorio-acesso" + relatorio.extensaoArquivo());
            Files.write(arquivo, conteudo);
            System.out.println("Relatório gerado em: " + arquivo.toAbsolutePath());
        } catch (RelatorioException e) {
            System.out.println("\nErro ao gerar relatório: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("\nErro ao salvar arquivo do relatório: " + e.getMessage());
        }
    }
}
