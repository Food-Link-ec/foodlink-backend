package com.foodlink.domain.model.comprador.exception;

public class CedulaInvalidaException extends RuntimeException {

    public CedulaInvalidaException(String mensaje) {
        super(mensaje);
    }
}