CREATE TABLE medico (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50) NOT NULL
);

CREATE TABLE paciente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(15)
);

CREATE TABLE cita (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    motivo TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    paciente_id BIGINT REFERENCES paciente(id),
    medico_id BIGINT REFERENCES medico(id)
);
