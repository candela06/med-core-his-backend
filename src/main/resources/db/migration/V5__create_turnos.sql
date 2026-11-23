CREATE TABLE turnos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profesional_id BIGINT NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    duracion_minutos INTEGER NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_turnos_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_turnos_profesional FOREIGN KEY (profesional_id) REFERENCES profesionales(id),
    CONSTRAINT ck_turnos_estado CHECK (estado IN ('PROGRAMADO','CONFIRMADO','CANCELADO','ATENDIDO','AUSENTE')),
    CONSTRAINT ck_turnos_duracion CHECK (duracion_minutos > 0)
);
CREATE INDEX idx_turnos_paciente_fecha ON turnos(paciente_id, fecha_hora);
CREATE INDEX idx_turnos_profesional_fecha ON turnos(profesional_id, fecha_hora);
