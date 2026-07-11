package com.foodlink.domain.model.comercio;

import com.foodlink.domain.model.comercio.exception.ComercioInvalidoException;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.model.shared.Email;
import com.foodlink.domain.model.shared.NombreOrganizacion;
import com.foodlink.domain.model.shared.RucEcuatoriano;
import com.foodlink.domain.model.shared.TelefonoEcuatoriano;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comercio {

    private final UUID id;
    private final RucEcuatoriano ruc;
    private final NombreOrganizacion nombre;
    private final TelefonoEcuatoriano telefono;
    private final Email email;
    private final Direccion direccion;
    private EstadoComercio estado;
    private final LocalDateTime fechaRegistro;

    private Comercio(UUID id, RucEcuatoriano ruc, NombreOrganizacion nombre, TelefonoEcuatoriano telefono, Email email,
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
        return new Comercio(
                UUID.randomUUID(),
                RucEcuatoriano.de(ruc),
                NombreOrganizacion.de(nombre),
                TelefonoEcuatoriano.de(telefono),
                Email.de(email),
                direccion,
                EstadoComercio.PENDIENTE_VERIFICACION,
                LocalDateTime.now()
        );
    }

    public static Comercio reconstituir(UUID id, String ruc, String nombre, String telefono, String email,
                                         Direccion direccion, EstadoComercio estado, LocalDateTime fechaRegistro) {
        return new Comercio(
                id,
                RucEcuatoriano.de(ruc),
                NombreOrganizacion.de(nombre),
                TelefonoEcuatoriano.de(telefono),
                Email.de(email),
                direccion,
                estado,
                fechaRegistro
        );
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

    public RucEcuatoriano getRuc() {
        return ruc;
    }

    public NombreOrganizacion getNombre() {
        return nombre;
    }

    public TelefonoEcuatoriano getTelefono() {
        return telefono;
    }

    public Email getEmail() {
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
