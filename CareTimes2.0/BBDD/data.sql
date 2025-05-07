-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    password VARCHAR(100),
    rol VARCHAR(50)
);

-- Crear tabla de citas
CREATE TABLE IF NOT EXISTS citas (
    id SERIAL PRIMARY KEY,
    paciente VARCHAR(100),
    doctor VARCHAR(100),
    fecha DATE,
    hora TIME,
    motivo TEXT
);

-- Insertar doctores
INSERT INTO usuarios (nombre, password, rol) VALUES ('dr.juarez', 'clave123', 'DOCTOR');
INSERT INTO usuarios (nombre, password, rol) VALUES ('dr.lopez', 'pass456', 'DOCTOR');

-- Insertar pacientes
INSERT INTO usuarios (nombre, password, rol) VALUES ('ana.perez', '1234', 'PACIENTE');
INSERT INTO usuarios (nombre, password, rol) VALUES ('carlos.gomez', 'abcd', 'PACIENTE');

-- Insertar citas
INSERT INTO citas (paciente, doctor, fecha, hora, motivo)
VALUES 
('Ana Pérez', 'Dr. Juan López', '2025-05-10', '09:30', 'Control general'),
('Luis Martínez', 'Dra. Carla Ruiz', '2025-05-11', '11:00', 'Dolor de cabeza'),
('María García', 'Dr. Pedro Gómez', '2025-05-12', '14:15', 'Chequeo anual');

