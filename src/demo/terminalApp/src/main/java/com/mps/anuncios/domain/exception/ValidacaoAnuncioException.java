package com.mps.anuncios.domain.exception;

import java.util.List;

public class ValidacaoAnuncioException extends RuntimeException {

    private final List<String> erros;

    public ValidacaoAnuncioException(List<String> erros) {
        super("Erros de validação: " + String.join(", ", erros));
        this.erros = List.copyOf(erros);
    }

    public List<String> getErros() {
        return erros;
    }
}
