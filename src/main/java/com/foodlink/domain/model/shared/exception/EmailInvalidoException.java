package com.foodlink.domain.model.shared.exception;

public class EmailInvalidoException extends RuntimeException {

    public EmailInvalidoException(String mensaje) {
        super(mensaje);
    }
}
