CREATE TABLE valoraciones (
    id            BIGSERIAL PRIMARY KEY,
    lote_id       UUID NOT NULL,
    comercio_id   UUID NOT NULL,
    puntuacion    INTEGER NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    comentario    VARCHAR(300),
    creado_en     TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_valoracion_comercio ON valoraciones(comercio_id);
CREATE INDEX idx_valoracion_lote ON valoraciones(lote_id);

INSERT INTO valoraciones (lote_id, comercio_id, puntuacion, comentario) VALUES
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567805',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567802',
    5,
    'Excelente calidad, llegamos a tiempo y todo estaba fresco.'
),
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567805',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
    4,
    'Muy buena experiencia, el pan estaba en buen estado.'
),
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567804',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
    5,
    'Los lácteos perfectos. Gracias por la donación.'
);
