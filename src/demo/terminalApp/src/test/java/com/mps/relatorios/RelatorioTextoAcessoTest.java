package com.mps.relatorios;

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

class RelatorioTextoAcessoTest {

    @Test
    void deve_gerar_texto_com_total_e_ranking() {
        UUID usuarioId = UUID.randomUUID();
        User usuario = new User(usuarioId, "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        List<AcessoLog> logs = List.of(
                new AcessoLog(UUID.randomUUID(), usuarioId, TipoAcesso.CRIACAO, Instant.now()),
                new AcessoLog(UUID.randomUUID(), usuarioId, TipoAcesso.BUSCA, Instant.now()));

        byte[] saida = new RelatorioTextoAcesso().gerar(logs, List.of(usuario));
        String texto = new String(saida, StandardCharsets.UTF_8);

        assertTrue(texto.contains("Total de acessos: 2"));
        assertTrue(texto.contains("joaosilva: 2"));
        assertTrue(texto.contains("CRIACAO"));
        assertTrue(texto.contains("BUSCA"));
    }

    @Test
    void deve_gerar_texto_quando_sem_logs() {
        byte[] saida = new RelatorioTextoAcesso().gerar(List.of(), List.of());
        String texto = new String(saida, StandardCharsets.UTF_8);

        assertTrue(texto.contains("Total de acessos: 0"));
    }

    @Test
    void extensaoArquivo_deve_ser_txt() {
        assertTrue(new RelatorioTextoAcesso().extensaoArquivo().equals(".txt"));
    }
}
