package com.mps.shared.exception;

public class RepositorioException extends RuntimeException {

    public RepositorioException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositorioException(String message) {
        super(message);
    }
}
