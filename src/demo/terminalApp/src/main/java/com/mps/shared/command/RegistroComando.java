package com.mps.shared.command;

import java.time.Instant;

public record RegistroComando(Instant momento, String descricao) {

    @Override
    public String toString() {
        return momento + " | " + descricao;
    }
}
