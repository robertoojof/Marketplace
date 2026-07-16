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

class RelatorioPdfAcessoTest {

    @Test
    void deve_gerar_bytes_de_pdf_valido() {
        UUID usuarioId = UUID.randomUUID();
        User usuario = new User(usuarioId, "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        List<AcessoLog> logs = List.of(
                new AcessoLog(UUID.randomUUID(), usuarioId, TipoAcesso.REMOCAO, Instant.now()));

        byte[] saida = new RelatorioPdfAcesso().gerar(logs, List.of(usuario));

        assertTrue(saida.length > 0);
        String cabecalho = new String(saida, 0, 5, StandardCharsets.US_ASCII);
        assertEquals("%PDF-", cabecalho);
    }

    @Test
    void extensaoArquivo_deve_ser_pdf() {
        assertEquals(".pdf", new RelatorioPdfAcesso().extensaoArquivo());
    }
}
