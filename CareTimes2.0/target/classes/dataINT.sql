-- Creación de médicos
INSERT INTO medico (nombre, especialidad) VALUES 
('Dr. Carlos García', 'Cardiología'),
('Dra. Ana Martínez', 'Pediatría'),
('Dr. Luis Rodríguez', 'Traumatología');

-- Creación de pacientes
INSERT INTO paciente (nombre, email, telefono) VALUES 
('Juan Pérez', 'juan.perez@example.com', '600123456'),
('María López', 'maria.lopez@example.com', '600654321'),
('Pedro Sánchez', 'pedro.sanchez@example.com', '600987654');

-- Creación de citas (asumiendo IDs 1 para médicos y pacientes)
INSERT INTO cita (fecha_hora, motivo, estado, paciente_id, medico_id) VALUES 
('2025-06-15 10:00:00', 'Revisión anual', 'PENDIENTE', 1, 1),
('2025-06-16 11:30:00', 'Dolor de espalda', 'PENDIENTE', 2, 3),
('2025-06-14 09:15:00', 'Vacunación infantil', 'COMPLETADA', 3, 2);
