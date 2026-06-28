package com.foodlink.domain.model.comprador.exception;

public class CompradorInvalidoException extends RuntimeException {

    public CompradorInvalidoException(String mensaje) {
        super(mensaje);
    }
}