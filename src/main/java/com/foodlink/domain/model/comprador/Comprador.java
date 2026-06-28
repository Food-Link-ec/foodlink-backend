package com.foodlink.domain.model.comprador;

import com.foodlink.domain.model.comprador.exception.CedulaInvalidaException;
import com.foodlink.domain.model.comprador.exception.CompradorInvalidoException;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

public class Comprador {

    private static final Pattern PATRON_CEDULA = Pattern.compile("\\d{10}");

    private final UUID id;
    private final String cedula;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String telefono;
    private final LocalDateTime fechaRegistro;
    private boolean activo;

    private Comprador(UUID id, String cedula, String nombre, String apellido, String email,
                       String telefono, LocalDateTime fechaRegistro, boolean activo) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public static Comprador registrar(String cedula, String nombre, String apellido, String email, String telefono) {
        if (cedula == null || cedula.isBlank()) {
            throw new CedulaInvalidaException("La cédula es obligatoria");
        }
        if (!validarCedula(cedula)) {
            throw new CedulaInvalidaException("La cédula '" + cedula + "' no es válida");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new CompradorInvalidoException("El nombre del comprador es obligatorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new CompradorInvalidoException("El apellido del comprador es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new CompradorInvalidoException("El email del comprador es obligatorio");
        }
        return new Comprador(
                UUID.randomUUID(),
                cedula,
                nombre,
                apellido,
                email,
                telefono,
                LocalDateTime.now(),
                true
        );
    }

    private static boolean validarCedula(String cedula) {
        if (!PATRON_CEDULA.matcher(cedula).matches()) {
            return false;
        }
        int codigoProvincia = Integer.parseInt(cedula.substring(0, 2));
        if (codigoProvincia < 1 || codigoProvincia > 24) {
            return false;
        }
        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito >= 6) {
            return false;
        }
        int suma = 0;
        for (int posicion = 0; posicion < 9; posicion++) {
            int digito = Character.getNumericValue(cedula.charAt(posicion));
            if (posicion % 2 == 0) {
                int valor = digito * 2;
                if (valor >= 10) {
                    valor -= 9;
                }
                suma += valor;
            } else {
                suma += digito;
            }
        }
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        return (suma + digitoVerificador) % 10 == 0;
    }

    public void desactivar() {
        if (!activo) {
            throw new CompradorInvalidoException("El comprador ya está inactivo");
        }
        activo = false;
    }

    public void activar() {
        if (activo) {
            throw new CompradorInvalidoException("El comprador ya está activo");
        }
        activo = true;
    }

    public boolean estaActivo() {
        return activo;
    }

    public UUID getId() {
        return id;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}