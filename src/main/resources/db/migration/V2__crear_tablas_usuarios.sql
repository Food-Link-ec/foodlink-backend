CREATE TABLE comercios (
    id UUID PRIMARY KEY,
    ruc VARCHAR(13) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    estado VARCHAR(30) NOT NULL,
    provincia VARCHAR(100),
    ciudad VARCHAR(100),
    calle_principal VARCHAR(200),
    calle_secundaria VARCHAR(200),
    referencia VARCHAR(300),
    fecha_registro TIMESTAMP NOT NULL
);

CREATE TABLE beneficiarios (
    id UUID PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    ruc VARCHAR(13) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    estado_verificacion VARCHAR(20) NOT NULL,
    provincia VARCHAR(100),
    ciudad VARCHAR(100),
    calle_principal VARCHAR(200),
    calle_secundaria VARCHAR(200),
    referencia VARCHAR(300),
    fecha_registro TIMESTAMP NOT NULL
);

CREATE TABLE compradores (
    id UUID PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL
);