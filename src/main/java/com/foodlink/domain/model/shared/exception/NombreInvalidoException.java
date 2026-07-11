package com.foodlink.domain.model.shared.exception;

public class NombreInvalidoException extends RuntimeException {

    public NombreInvalidoException(String mensaje) {
        super(mensaje);
    }
}
