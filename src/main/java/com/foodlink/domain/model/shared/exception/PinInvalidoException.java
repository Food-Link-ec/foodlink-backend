package com.foodlink.domain.model.shared.exception;

public class PinInvalidoException extends RuntimeException {

    public PinInvalidoException(String mensaje) {
        super(mensaje);
    }
}