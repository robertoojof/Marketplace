package com.mps.acessos.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acesso_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AcessoLog {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "acao", nullable = false)
    private TipoAcesso acao;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
