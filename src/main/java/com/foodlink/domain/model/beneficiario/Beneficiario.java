package com.foodlink.domain.model.beneficiario;

import com.foodlink.domain.model.beneficiario.exception.BeneficiarioInvalidoException;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.model.shared.Email;
import com.foodlink.domain.model.shared.NombreOrganizacion;
import com.foodlink.domain.model.shared.RucEcuatoriano;
import com.foodlink.domain.model.shared.TelefonoEcuatoriano;

import java.time.LocalDateTime;
import java.util.UUID;

public class Beneficiario {

    private final UUID id;
    private final NombreOrganizacion nombre;
    private final RucEcuatoriano ruc;
    private final Email email;
    private final TelefonoEcuatoriano telefono;
    private final Direccion direccion;
    private EstadoVerificacion estadoVerificacion;
    private final LocalDateTime fechaRegistro;

    private Beneficiario(UUID id, NombreOrganizacion nombre, RucEcuatoriano ruc, Email email, TelefonoEcuatoriano telefono,
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
        return new Beneficiario(
                UUID.randomUUID(),
                NombreOrganizacion.de(nombre),
                RucEcuatoriano.de(ruc),
                Email.de(email),
                TelefonoEcuatoriano.de(telefono),
                direccion,
                EstadoVerificacion.PENDIENTE,
                LocalDateTime.now()
        );
    }

    public static Beneficiario reconstituir(UUID id, String nombre, String ruc, String email, String telefono,
                                             Direccion direccion, EstadoVerificacion estadoVerificacion, LocalDateTime fechaRegistro) {
        return new Beneficiario(
                id,
                NombreOrganizacion.de(nombre),
                RucEcuatoriano.de(ruc),
                Email.de(email),
                TelefonoEcuatoriano.de(telefono),
                direccion,
                estadoVerificacion,
                fechaRegistro
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

    public NombreOrganizacion getNombre() {
        return nombre;
    }

    public RucEcuatoriano getRuc() {
        return ruc;
    }

    public Email getEmail() {
        return email;
    }

    public TelefonoEcuatoriano getTelefono() {
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
