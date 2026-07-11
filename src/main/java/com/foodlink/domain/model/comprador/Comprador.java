package com.foodlink.domain.model.comprador;

import com.foodlink.domain.model.comprador.exception.CompradorInvalidoException;
import com.foodlink.domain.model.shared.CedulaEcuatoriana;
import com.foodlink.domain.model.shared.Email;
import com.foodlink.domain.model.shared.NombrePersona;
import com.foodlink.domain.model.shared.TelefonoEcuatoriano;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comprador {

    private final UUID id;
    private final CedulaEcuatoriana cedula;
    private final NombrePersona nombre;
    private final NombrePersona apellido;
    private final Email email;
    private final TelefonoEcuatoriano telefono;
    private final LocalDateTime fechaRegistro;
    private boolean activo;

    private Comprador(UUID id, CedulaEcuatoriana cedula, NombrePersona nombre, NombrePersona apellido, Email email,
                       TelefonoEcuatoriano telefono, LocalDateTime fechaRegistro, boolean activo) {
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
        return new Comprador(
                UUID.randomUUID(),
                CedulaEcuatoriana.de(cedula),
                NombrePersona.de(nombre),
                NombrePersona.de(apellido),
                Email.de(email),
                telefono == null || telefono.isBlank() ? null : TelefonoEcuatoriano.de(telefono),
                LocalDateTime.now(),
                true
        );
    }

    public static Comprador reconstituir(UUID id, String cedula, String nombre, String apellido, String email,
                                          String telefono, LocalDateTime fechaRegistro, boolean activo) {
        return new Comprador(
                id,
                CedulaEcuatoriana.de(cedula),
                NombrePersona.de(nombre),
                NombrePersona.de(apellido),
                Email.de(email),
                telefono == null || telefono.isBlank() ? null : TelefonoEcuatoriano.de(telefono),
                fechaRegistro,
                activo
        );
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

    public CedulaEcuatoriana getCedula() {
        return cedula;
    }

    public NombrePersona getNombre() {
        return nombre;
    }

    public NombrePersona getApellido() {
        return apellido;
    }

    public Email getEmail() {
        return email;
    }

    public TelefonoEcuatoriano getTelefono() {
        return telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
