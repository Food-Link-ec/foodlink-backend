package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.EmailInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void deberiaCrearseConEmailValidoSimple() {
        Email email = Email.de("contacto@foodlink.com");

        assertEquals("contacto@foodlink.com", email.valor());
    }

    @Test
    void deberiaCrearseConEmailConSubdominio() {
        Email email = Email.de("soporte@mail.foodlink.com.ec");

        assertEquals("soporte@mail.foodlink.com.ec", email.valor());
    }

    @Test
    void deberiaFallarSinArroba() {
        assertThrows(EmailInvalidoException.class, () -> Email.de("contactofoodlink.com"));
    }

    @Test
    void deberiaFallarConDosArrobas() {
        assertThrows(EmailInvalidoException.class, () -> Email.de("contacto@@foodlink.com"));
    }

    @Test
    void deberiaFallarConDominioSinPunto() {
        assertThrows(EmailInvalidoException.class, () -> Email.de("contacto@foodlink"));
    }

    @Test
    void deberiaFallarConEmailVacio() {
        assertThrows(EmailInvalidoException.class, () -> Email.de(""));
    }

    @Test
    void deberiaGuardarseEnMinusculas() {
        Email email = Email.de("Contacto@FoodLink.COM");

        assertEquals("contacto@foodlink.com", email.valor());
    }
}
