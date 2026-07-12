package com.foodlink.domain.model.lote;

import com.foodlink.domain.event.DonacionRealizada;
import com.foodlink.domain.event.EntregaConfirmada;
import com.foodlink.domain.event.LoteExpirado;
import com.foodlink.domain.event.LotePublicado;
import com.foodlink.domain.event.LoteReservado;
import com.foodlink.domain.event.ReservaCancelada;
import com.foodlink.domain.event.VentaRealizada;
import com.foodlink.domain.model.lote.exception.LoteNoDisponibleException;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoteExcedente {

    private final UUID id;
    private final UUID comercioId;
    private final Modalidad modalidad;
    private EstadoLote estado;
    private final double cantidadKg;
    private final Dinero precio;
    private final FechaCaducidad fechaCaducidad;
    private LocalDateTime fechaPublicacion;
    private final String descripcion;
    private final List<String> fotosUrl;
    private UUID beneficiarioReservaId;
    private LocalDateTime inicioReserva;
    private Double latitud;
    private Double longitud;
    private final List<Object> eventos;

    private LoteExcedente(UUID id, UUID comercioId, Modalidad modalidad, EstadoLote estado, double cantidadKg,
                           Dinero precio, FechaCaducidad fechaCaducidad, LocalDateTime fechaPublicacion,
                           String descripcion, List<String> fotosUrl, UUID beneficiarioReservaId,
                           LocalDateTime inicioReserva, Double latitud, Double longitud, List<Object> eventos) {
        this.id = id;
        this.comercioId = comercioId;
        this.modalidad = modalidad;
        this.estado = estado;
        this.cantidadKg = cantidadKg;
        this.precio = precio;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaPublicacion = fechaPublicacion;
        this.descripcion = descripcion;
        this.fotosUrl = fotosUrl;
        this.beneficiarioReservaId = beneficiarioReservaId;
        this.inicioReserva = inicioReserva;
        this.latitud = latitud;
        this.longitud = longitud;
        this.eventos = eventos;
    }

    static LoteExcedente crear(UUID comercioId, Modalidad modalidad, double cantidadKg, Dinero precio,
                                FechaCaducidad fechaCaducidad, String descripcion, List<String> fotosUrl) {
        return new LoteExcedente(
                UUID.randomUUID(),
                comercioId,
                modalidad,
                EstadoLote.BORRADOR,
                cantidadKg,
                precio,
                fechaCaducidad,
                null,
                descripcion,
                fotosUrl,
                null,
                null,
                null,
                null,
                new ArrayList<>()
        );
    }

    public static LoteExcedente reconstituir(UUID id, UUID comercioId, Modalidad modalidad, EstadoLote estado,
                                              double cantidadKg, Dinero precio, FechaCaducidad fechaCaducidad,
                                              LocalDateTime fechaPublicacion, String descripcion,
                                              List<String> fotosUrl, UUID beneficiarioReservaId,
                                              LocalDateTime inicioReserva, Double latitud, Double longitud) {
        return new LoteExcedente(id, comercioId, modalidad, estado, cantidadKg, precio, fechaCaducidad,
                fechaPublicacion, descripcion, fotosUrl, beneficiarioReservaId, inicioReserva, latitud, longitud,
                new ArrayList<>());
    }

    void registrarEvento(Object evento) {
        eventos.add(evento);
    }

    public void publicar() {
        if (estado != EstadoLote.BORRADOR && estado != EstadoLote.PENDIENTE_VALIDACION) {
            throw new LoteNoDisponibleException("Solo un lote en borrador o pendiente de validación puede publicarse");
        }
        if (fechaCaducidad.horasRestantes() < 24) {
            throw new LoteNoDisponibleException("El lote debe tener al menos 24 horas antes de caducar para publicarse");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new LoteNoDisponibleException("El lote debe tener una descripción para publicarse");
        }
        if (fotosUrl == null || fotosUrl.isEmpty()) {
            throw new LoteNoDisponibleException("El lote debe tener al menos una foto para publicarse");
        }
        estado = EstadoLote.DISPONIBLE;
        fechaPublicacion = LocalDateTime.now();
        eventos.add(new LotePublicado(id, comercioId, modalidad, cantidadKg, LocalDateTime.now()));
    }

    public void reservar(UUID beneficiarioId) {
        if (estado != EstadoLote.DISPONIBLE) {
            throw new LoteNoDisponibleException("Solo un lote disponible puede reservarse");
        }
        if (fechaCaducidad.haExpirado()) {
            throw new LoteNoDisponibleException("El lote ya caducó y no puede reservarse");
        }
        if (fechaCaducidad.horasRestantes() < 2) {
            throw new LoteNoDisponibleException("El lote debe tener al menos 2 horas antes de caducar para reservarse");
        }
        estado = EstadoLote.RESERVADO;
        beneficiarioReservaId = beneficiarioId;
        inicioReserva = LocalDateTime.now();
        eventos.add(new LoteReservado(id, beneficiarioId, inicioReserva, LocalDateTime.now()));
    }

    public void cancelarReserva(String motivo) {
        if (estado != EstadoLote.RESERVADO) {
            throw new LoteNoDisponibleException("Solo un lote reservado puede cancelar su reserva");
        }
        UUID beneficiarioId = beneficiarioReservaId;
        estado = EstadoLote.DISPONIBLE;
        beneficiarioReservaId = null;
        inicioReserva = null;
        eventos.add(new ReservaCancelada(id, beneficiarioId, motivo, LocalDateTime.now()));
    }

    public void registrarVenta(UUID compradorId) {
        if (estado != EstadoLote.RESERVADO) {
            throw new LoteNoDisponibleException("Solo un lote reservado puede registrarse como vendido");
        }
        if (modalidad != Modalidad.VENTA) {
            throw new LoteNoDisponibleException("Solo un lote en modalidad venta puede registrarse como vendido");
        }
        if (precio == null || precio.esCero()) {
            throw new LoteNoDisponibleException("El lote debe tener un precio válido para registrarse como vendido");
        }
        estado = EstadoLote.VENDIDO;
        eventos.add(new VentaRealizada(id, compradorId, precio.getMonto(), LocalDateTime.now()));
    }

    public void registrarDonacion(UUID organizacionId) {
        if (estado != EstadoLote.RESERVADO) {
            throw new LoteNoDisponibleException("Solo un lote reservado puede registrarse como donado");
        }
        if (modalidad != Modalidad.DONACION) {
            throw new LoteNoDisponibleException("Solo un lote en modalidad donación puede registrarse como donado");
        }
        estado = EstadoLote.DONADO;
        eventos.add(new DonacionRealizada(id, organizacionId, LocalDateTime.now()));
    }

    public void confirmarEntrega(UUID receptorId) {
        if (estado != EstadoLote.VENDIDO && estado != EstadoLote.DONADO) {
            throw new LoteNoDisponibleException("Solo un lote vendido o donado puede confirmarse como entregado");
        }
        estado = EstadoLote.ENTREGADO;
        eventos.add(new EntregaConfirmada(id, comercioId, receptorId, cantidadKg, LocalDateTime.now()));
    }

    public void expirar() {
        if (estado == EstadoLote.EXPIRADO || estado == EstadoLote.ENTREGADO) {
            return;
        }
        estado = EstadoLote.EXPIRADO;
        eventos.add(new LoteExpirado(id, comercioId, fechaCaducidad.getValor(), LocalDateTime.now()));
    }

    public void rechazar(String motivo) {
        if (estado != EstadoLote.PENDIENTE_VALIDACION) {
            throw new LoteNoDisponibleException("Solo un lote pendiente de validación puede rechazarse");
        }
        estado = EstadoLote.RECHAZADO;
    }

    public void asignarUbicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    boolean tieneUbicacion() {
        return latitud != null && longitud != null;
    }

    public boolean estaDisponible() {
        return estado == EstadoLote.DISPONIBLE && !fechaCaducidad.haExpirado();
    }

    public boolean estaReservado() {
        return estado == EstadoLote.RESERVADO;
    }

    public boolean fueEntregado() {
        return estado == EstadoLote.ENTREGADO;
    }

    public boolean haExpirado() {
        return fechaCaducidad.haExpirado() || estado == EstadoLote.EXPIRADO;
    }

    public List<Object> pullEventos() {
        List<Object> copia = new ArrayList<>(eventos);
        eventos.clear();
        return copia;
    }

    public UUID getId() {
        return id;
    }

    public UUID getComercioId() {
        return comercioId;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public EstadoLote getEstado() {
        return estado;
    }

    public double getCantidadKg() {
        return cantidadKg;
    }

    public Dinero getPrecio() {
        return precio;
    }

    public FechaCaducidad getFechaCaducidad() {
        return fechaCaducidad;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<String> getFotosUrl() {
        return fotosUrl;
    }

    public UUID getBeneficiarioReservaId() {
        return beneficiarioReservaId;
    }

    public LocalDateTime getInicioReserva() {
        return inicioReserva;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }
}
