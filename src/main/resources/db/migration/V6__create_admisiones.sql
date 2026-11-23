CREATE TABLE admisiones (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    fecha_ingreso TIMESTAMP NOT NULL,
    tipo_ingreso VARCHAR(20) NOT NULL,
    motivo_consulta VARCHAR(500) NOT NULL,
    profesional_id BIGINT,
    turno_id BIGINT,
    estado VARCHAR(20) NOT NULL DEFAULT 'EN_ESPERA',
    CONSTRAINT fk_admisiones_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_admisiones_profesional FOREIGN KEY (profesional_id) REFERENCES profesionales(id),
    CONSTRAINT fk_admisiones_turno FOREIGN KEY (turno_id) REFERENCES turnos(id),
    CONSTRAINT ck_admisiones_tipo CHECK (tipo_ingreso IN ('PROGRAMADO','ESPONTANEO')),
    CONSTRAINT ck_admisiones_estado CHECK (estado IN ('EN_ESPERA','EN_ATENCION','FINALIZADA','CANCELADA'))
);
CREATE INDEX idx_admisiones_paciente_estado ON admisiones(paciente_id, estado);
