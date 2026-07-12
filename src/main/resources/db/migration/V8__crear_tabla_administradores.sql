CREATE TABLE administradores (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre        VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO administradores (id, nombre, email, password_hash, activo) VALUES (
    'f0000000-0000-0000-0000-000000000001',
    'Administrador FoodLink',
    'admin@foodlink.ec',
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    true
);
