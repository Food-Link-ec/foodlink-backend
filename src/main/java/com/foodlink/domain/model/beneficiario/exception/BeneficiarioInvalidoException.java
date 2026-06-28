package com.foodlink.domain.model.beneficiario.exception;

public class BeneficiarioInvalidoException extends RuntimeException {

    public BeneficiarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}