ALTER TABLE comercios
    ADD COLUMN password_hash VARCHAR(255),
    ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE beneficiarios
    ADD COLUMN password_hash VARCHAR(255),
    ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE compradores
    ADD COLUMN password_hash VARCHAR(255);

CREATE TABLE refresh_tokens (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id    UUID NOT NULL,
    tipo_usuario  VARCHAR(20) NOT NULL,
    token_hash    VARCHAR(255) NOT NULL UNIQUE,
    expira_en     TIMESTAMP NOT NULL,
    creado_en     TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_tipo_usuario CHECK (tipo_usuario IN ('COMERCIO','BENEFICIARIO','COMPRADOR'))
);

CREATE INDEX idx_refresh_token_hash ON refresh_tokens(token_hash);
CREATE INDEX idx_refresh_token_usuario ON refresh_tokens(usuario_id);
