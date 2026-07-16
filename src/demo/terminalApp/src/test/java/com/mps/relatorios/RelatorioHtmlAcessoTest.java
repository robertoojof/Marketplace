package com.mps.relatorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.TipoAcesso;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class RelatorioHtmlAcessoTest {

    @Test
    void deve_gerar_html_valido_com_tabela_de_ranking() {
        UUID usuarioId = UUID.randomUUID();
        User usuario = new User(usuarioId, "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        List<AcessoLog> logs = List.of(
                new AcessoLog(UUID.randomUUID(), usuarioId, TipoAcesso.ATUALIZACAO, Instant.now()));

        byte[] saida = new RelatorioHtmlAcesso().gerar(logs, List.of(usuario));
        String html = new String(saida, StandardCharsets.UTF_8);

        assertTrue(html.startsWith("<html>"));
        assertTrue(html.endsWith("</html>"));
        assertTrue(html.contains("<table"));
        assertTrue(html.contains("joaosilva"));
        assertTrue(html.contains("Total de acessos: <strong>1</strong>"));
    }

    @Test
    void extensaoArquivo_deve_ser_html() {
        assertEquals(".html", new RelatorioHtmlAcesso().extensaoArquivo());
    }
}
