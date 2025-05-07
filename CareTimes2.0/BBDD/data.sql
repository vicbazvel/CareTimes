CREATE TABLE citas (
    id SERIAL PRIMARY KEY,
    paciente VARCHAR(100),
    doctor VARCHAR(100),
    fecha DATE,
    hora TIME,
    motivo TEXT
);

