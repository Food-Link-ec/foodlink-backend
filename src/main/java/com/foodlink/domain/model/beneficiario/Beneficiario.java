package com.foodlink.domain.model.beneficiario;

import com.foodlink.domain.model.beneficiario.exception.BeneficiarioInvalidoException;
import com.foodlink.domain.model.shared.Direccion;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

public class Beneficiario {

    private static final Pattern PATRON_RUC = Pattern.compile("\\d{13}");

    private final UUID id;
    private final String nombre;
    private final String ruc;
    private final String email;
    private final String telefono;
    private final Direccion direccion;
    private EstadoVerificacion estadoVerificacion;
    private final LocalDateTime fechaRegistro;

    private Beneficiario(UUID id, String nombre, String ruc, String email, String telefono,
                          Direccion direccion, EstadoVerificacion estadoVerificacion, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estadoVerificacion = estadoVerificacion;
        this.fechaRegistro = fechaRegistro;
    }

    public static Beneficiario registrar(String nombre, String ruc, String email, String telefono, Direccion direccion) {
        if (nombre == null || nombre.isBlank()) {
            throw new BeneficiarioInvalidoException("El nombre del beneficiario es obligatorio");
        }
        if (ruc == null || ruc.isBlank()) {
            throw new BeneficiarioInvalidoException("El RUC del beneficiario es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new BeneficiarioInvalidoException("El email del beneficiario es obligatorio");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new BeneficiarioInvalidoException("El teléfono del beneficiario es obligatorio");
        }
        if (!PATRON_RUC.matcher(ruc).matches()) {
            throw new BeneficiarioInvalidoException("El RUC '" + ruc + "' debe tener exactamente 13 dígitos numéricos");
        }
        return new Beneficiario(
                UUID.randomUUID(),
                nombre,
                ruc,
                email,
                telefono,
                direccion,
                EstadoVerificacion.PENDIENTE,
                LocalDateTime.now()
        );
    }

    public void verificar() {
        if (estadoVerificacion != EstadoVerificacion.PENDIENTE) {
            throw new BeneficiarioInvalidoException("Solo un beneficiario pendiente puede ser verificado");
        }
        estadoVerificacion = EstadoVerificacion.VERIFICADO;
    }

    public void rechazar(String motivo) {
        if (estadoVerificacion != EstadoVerificacion.PENDIENTE) {
            throw new BeneficiarioInvalidoException("Solo un beneficiario pendiente puede ser rechazado");
        }
        estadoVerificacion = EstadoVerificacion.RECHAZADO;
    }

    public boolean estaVerificado() {
        return estadoVerificacion == EstadoVerificacion.VERIFICADO;
    }

    public boolean puedeRecibirDonaciones() {
        return estadoVerificacion == EstadoVerificacion.VERIFICADO;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public EstadoVerificacion getEstadoVerificacion() {
        return estadoVerificacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}