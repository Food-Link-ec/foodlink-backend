package com.foodlink.domain.model.comercio;

import com.foodlink.domain.model.comercio.exception.ComercioInvalidoException;
import com.foodlink.domain.model.comercio.exception.RucInvalidoException;
import com.foodlink.domain.model.shared.Direccion;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

public class Comercio {

    private static final Pattern PATRON_RUC = Pattern.compile("\\d{13}");

    private final UUID id;
    private final String ruc;
    private final String nombre;
    private final String telefono;
    private final String email;
    private final Direccion direccion;
    private EstadoComercio estado;
    private final LocalDateTime fechaRegistro;

    private Comercio(UUID id, String ruc, String nombre, String telefono, String email,
                      Direccion direccion, EstadoComercio estado, LocalDateTime fechaRegistro) {
        this.id = id;
        this.ruc = ruc;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public static Comercio crear(String ruc, String nombre, String telefono, String email, Direccion direccion) {
        if (!validarRuc(ruc)) {
            throw new RucInvalidoException("El RUC '" + ruc + "' no es válido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new ComercioInvalidoException("El nombre del comercio es obligatorio");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new ComercioInvalidoException("El teléfono del comercio es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new ComercioInvalidoException("El email del comercio es obligatorio");
        }
        return new Comercio(
                UUID.randomUUID(),
                ruc,
                nombre,
                telefono,
                email,
                direccion,
                EstadoComercio.PENDIENTE_VERIFICACION,
                LocalDateTime.now()
        );
    }

    public static Comercio reconstituir(UUID id, String ruc, String nombre, String telefono, String email,
                                         Direccion direccion, EstadoComercio estado, LocalDateTime fechaRegistro) {
        return new Comercio(id, ruc, nombre, telefono, email, direccion, estado, fechaRegistro);
    }

    private static boolean validarRuc(String ruc) {
        if (ruc == null || !PATRON_RUC.matcher(ruc).matches()) {
            return false;
        }
        int codigoProvincia = Integer.parseInt(ruc.substring(0, 2));
        if (codigoProvincia < 1 || codigoProvincia > 24) {
            return false;
        }
        char tercerDigito = ruc.charAt(2);
        if (tercerDigito != '6' && tercerDigito != '9') {
            return false;
        }
        return ruc.substring(10, 13).equals("001");
    }

    public void verificar() {
        if (estado != EstadoComercio.PENDIENTE_VERIFICACION) {
            throw new ComercioInvalidoException("Solo un comercio pendiente de verificación puede ser verificado");
        }
        estado = EstadoComercio.VERIFICADO;
    }

    public void rechazar(String motivo) {
        if (estado != EstadoComercio.PENDIENTE_VERIFICACION) {
            throw new ComercioInvalidoException("Solo un comercio pendiente de verificación puede ser rechazado");
        }
        estado = EstadoComercio.RECHAZADO;
    }

    public void suspender() {
        if (estado != EstadoComercio.VERIFICADO) {
            throw new ComercioInvalidoException("Solo un comercio verificado puede ser suspendido");
        }
        estado = EstadoComercio.SUSPENDIDO;
    }

    public boolean estaVerificado() {
        return estado == EstadoComercio.VERIFICADO;
    }

    public boolean estaActivo() {
        return estado == EstadoComercio.VERIFICADO || estado == EstadoComercio.PENDIENTE_VERIFICACION;
    }

    public UUID getId() {
        return id;
    }

    public String getRuc() {
        return ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public EstadoComercio getEstado() {
        return estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}