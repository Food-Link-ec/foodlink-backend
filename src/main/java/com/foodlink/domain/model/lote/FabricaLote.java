package com.foodlink.domain.model.lote;

import com.foodlink.domain.event.LoteRegistrado;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class FabricaLote {

    private FabricaLote() {
    }

    public static LoteExcedente crearParaVenta(UUID comercioId, double cantidadKg, Dinero precio,
                                                Dinero precioMercado, FechaCaducidad fechaCaducidad,
                                                String descripcion, List<String> fotosUrl) {
        validarCantidad(cantidadKg);
        precio.validarPrecioVenta(precioMercado);
        LoteExcedente lote = LoteExcedente.crear(comercioId, Modalidad.VENTA, cantidadKg, precio,
                precioMercado.getMonto(), fechaCaducidad, descripcion, fotosUrl);
        lote.registrarEvento(new LoteRegistrado(lote.getId(), comercioId, Modalidad.VENTA, LocalDateTime.now()));
        return lote;
    }

    public static LoteExcedente crearParaDonacion(UUID comercioId, double cantidadKg,
                                                   FechaCaducidad fechaCaducidad, String descripcion,
                                                   List<String> fotosUrl) {
        validarCantidad(cantidadKg);
        Dinero precio = Dinero.cero();
        LoteExcedente lote = LoteExcedente.crear(comercioId, Modalidad.DONACION, cantidadKg, precio,
                null, fechaCaducidad, descripcion, fotosUrl);
        lote.registrarEvento(new LoteRegistrado(lote.getId(), comercioId, Modalidad.DONACION, LocalDateTime.now()));
        return lote;
    }

    public static LoteExcedente crearParaRetiroDirecto(UUID comercioId, double cantidadKg,
                                                         FechaCaducidad fechaCaducidad, String descripcion,
                                                         List<String> fotosUrl) {
        validarCantidad(cantidadKg);
        Dinero precio = Dinero.cero();
        LoteExcedente lote = LoteExcedente.crear(comercioId, Modalidad.RETIRO_DIRECTO, cantidadKg, precio,
                null, fechaCaducidad, descripcion, fotosUrl);
        lote.registrarEvento(new LoteRegistrado(lote.getId(), comercioId, Modalidad.RETIRO_DIRECTO, LocalDateTime.now()));
        return lote;
    }

    private static void validarCantidad(double cantidadKg) {
        if (cantidadKg <= 0) {
            throw new IllegalArgumentException("La cantidad en kilogramos debe ser mayor a cero");
        }
    }
}
