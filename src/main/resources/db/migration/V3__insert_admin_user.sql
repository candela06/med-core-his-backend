-- V3__insert_admin_user.sql

INSERT INTO usuarios (username, password_hash, rol, activo, fecha_creacion)
SELECT 'admin', '$2a$12$h1/RMzTGo6tC9vm33KKAMueSWM8Nk0Aayx5uBsXsA0iN5UdcsVFKi', 'ADMIN', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE username = 'admin'
);