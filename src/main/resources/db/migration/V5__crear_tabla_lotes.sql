CREATE TABLE lotes_excedentes (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    comercio_id             UUID NOT NULL,
    modalidad               VARCHAR(20) NOT NULL,
    estado                  VARCHAR(30) NOT NULL,
    cantidad_kg             DECIMAL(10,3) NOT NULL CHECK (cantidad_kg > 0),
    precio_monto            DECIMAL(10,2),
    precio_moneda           VARCHAR(3),
    fecha_caducidad         TIMESTAMP NOT NULL,
    fecha_publicacion       TIMESTAMP,
    descripcion             VARCHAR(500),
    beneficiario_reserva_id UUID,
    inicio_reserva          TIMESTAMP,
    creado_en               TIMESTAMP NOT NULL DEFAULT NOW(),
    actualizado_en          TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_modalidad CHECK (modalidad IN (
        'VENTA', 'DONACION', 'RETIRO_DIRECTO'
    )),
    CONSTRAINT chk_estado CHECK (estado IN (
        'BORRADOR', 'PENDIENTE_VALIDACION', 'DISPONIBLE',
        'RESERVADO', 'VENDIDO', 'DONADO', 'ENTREGADO',
        'EXPIRADO', 'RECHAZADO'
    ))
);

CREATE TABLE lote_fotos (
    id      BIGSERIAL PRIMARY KEY,
    lote_id UUID NOT NULL REFERENCES lotes_excedentes(id) ON DELETE CASCADE,
    url     VARCHAR(500) NOT NULL
);

CREATE TABLE impacto_metricas (
    id                    BIGSERIAL PRIMARY KEY,
    lote_id               UUID NOT NULL,
    cantidad_kg           DECIMAL(10,3) NOT NULL,
    co2_evitado_kg        DECIMAL(10,3) NOT NULL,
    personas_beneficiadas INTEGER NOT NULL,
    fecha_entrega         TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_lote_estado     ON lotes_excedentes(estado);
CREATE INDEX idx_lote_comercio   ON lotes_excedentes(comercio_id);
CREATE INDEX idx_lote_modalidad  ON lotes_excedentes(modalidad);
CREATE INDEX idx_lote_caducidad  ON lotes_excedentes(fecha_caducidad);