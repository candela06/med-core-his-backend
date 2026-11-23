CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profesional_id BIGINT NOT NULL,
    admision_id BIGINT NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    motivo_consulta VARCHAR(500) NOT NULL,
    anamnesis VARCHAR(3000),
    observaciones_clinicas VARCHAR(3000),
    diagnostico VARCHAR(3000),
    estado VARCHAR(20) NOT NULL DEFAULT 'EN_CURSO',
    autor VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_consultas_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_consultas_profesional FOREIGN KEY (profesional_id) REFERENCES profesionales(id),
    CONSTRAINT fk_consultas_admision FOREIGN KEY (admision_id) REFERENCES admisiones(id),
    CONSTRAINT ck_consultas_estado CHECK (estado IN ('EN_CURSO','FINALIZADA'))
);
CREATE INDEX idx_consultas_paciente ON consultas(paciente_id, fecha_hora);
