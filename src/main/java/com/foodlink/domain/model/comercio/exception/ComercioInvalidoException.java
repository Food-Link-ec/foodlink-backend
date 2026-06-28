package com.foodlink.domain.model.comercio.exception;

public class ComercioInvalidoException extends RuntimeException {

    public ComercioInvalidoException(String mensaje) {
        super(mensaje);
    }
}