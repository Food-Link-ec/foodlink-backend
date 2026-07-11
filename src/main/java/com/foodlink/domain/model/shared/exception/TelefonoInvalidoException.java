package com.foodlink.domain.model.shared.exception;

public class TelefonoInvalidoException extends RuntimeException {

    public TelefonoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
