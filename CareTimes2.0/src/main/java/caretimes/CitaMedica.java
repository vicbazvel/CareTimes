package citas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CitaMedica {
    private long id;
    private String paciente;
    private String doctor;
    private String fecha; // Formato: "YYYY-MM-DD"
    private String hora;  // Formato: "HH:mm"
    private String motivo;

    public CitaMedica() {
    }

    public CitaMedica(long id, String paciente, String doctor, String fecha, String hora, String motivo) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    public long getId() {
        return id;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getMotivo() {
        return motivo;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    @JsonProperty
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @JsonProperty
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @JsonProperty
    public void setHora(String hora) {
        this.hora = hora;
    }

    @JsonProperty
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}

