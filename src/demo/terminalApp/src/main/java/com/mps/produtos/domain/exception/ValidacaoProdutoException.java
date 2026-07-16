package com.mps.produtos.domain.exception;

import java.util.List;

public class ValidacaoProdutoException extends RuntimeException {

    private final List<String> erros;

    public ValidacaoProdutoException(List<String> erros) {
        super("Erros de validação: " + String.join(", ", erros));
        this.erros = List.copyOf(erros);
    }

    public List<String> getErros() {
        return erros;
    }
}
