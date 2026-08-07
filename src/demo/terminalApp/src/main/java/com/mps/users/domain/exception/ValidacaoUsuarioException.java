package com.mps.users.domain.exception;

import com.mps.shared.exception.NegocioException;

import java.util.List;

public class ValidacaoUsuarioException extends NegocioException {

    private final List<String> erros;

    public ValidacaoUsuarioException(List<String> erros) {
        super("Erros de validação: " + String.join(", ", erros));
        this.erros = List.copyOf(erros);
    }

    public List<String> getErros() {
        return erros;
    }
}
