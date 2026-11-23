CREATE TABLE pacientes (
    id BIGSERIAL PRIMARY KEY,
    documento VARCHAR(30) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(20) NOT NULL,
    telefono VARCHAR(30),
    correo_electronico VARCHAR(150),
    domicilio VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT uk_pacientes_documento UNIQUE (documento),
    CONSTRAINT ck_pacientes_sexo CHECK (sexo IN ('FEMENINO', 'MASCULINO', 'OTRO', 'NO_ESPECIFICADO')),
    CONSTRAINT ck_pacientes_estado CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE INDEX idx_pacientes_apellido ON pacientes(apellido);
