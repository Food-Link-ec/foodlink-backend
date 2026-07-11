CREATE TABLE pins_retiro (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    lote_id       UUID NOT NULL UNIQUE,
    pin           VARCHAR(5) NOT NULL,
    qr_data       TEXT NOT NULL,
    usado         BOOLEAN NOT NULL DEFAULT FALSE,
    expira_en     TIMESTAMP NOT NULL,
    creado_en     TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pin_lote ON pins_retiro(lote_id);
CREATE INDEX idx_pin_valor ON pins_retiro(pin);