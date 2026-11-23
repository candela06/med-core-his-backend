CREATE TABLE auditoria (
 id BIGSERIAL PRIMARY KEY,
 usuario VARCHAR(100),
 accion VARCHAR(100) NOT NULL,
 entidad VARCHAR(100) NOT NULL,
 entidad_id BIGINT,
 detalle VARCHAR(3000),
 fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_auditoria_fecha ON auditoria(fecha_hora);
