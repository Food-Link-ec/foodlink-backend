package com.foodlink.domain.port.output;

import com.foodlink.application.dto.request.AnalizarImagenRequest;
import com.foodlink.application.dto.response.AnalisisImagenResponse;

public interface IServicioIA {

    AnalisisImagenResponse analizarImagen(AnalizarImagenRequest request);
}
