CREATE TABLE profesionales (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    matricula VARCHAR(50) NOT NULL,
    especialidad VARCHAR(30) NOT NULL,
    telefono VARCHAR(30),
    correo_electronico VARCHAR(150),
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_profesionales_matricula UNIQUE (matricula),
    CONSTRAINT ck_profesionales_especialidad CHECK (especialidad IN ('CLINICA_MEDICA', 'CARDIOLOGIA', 'PEDIATRIA', 'TRAUMATOLOGIA', 'DERMATOLOGIA')),
    CONSTRAINT ck_profesionales_estado CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE INDEX idx_profesionales_apellido ON profesionales(apellido);
CREATE INDEX idx_profesionales_especialidad ON profesionales(especialidad);

ALTER TABLE usuarios
    ADD CONSTRAINT fk_usuarios_profesional
    FOREIGN KEY (profesional_id) REFERENCES profesionales(id);
