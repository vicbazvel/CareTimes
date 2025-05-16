package citas;

public class CitaMedica {
    private long id;
    private String paciente;
    private String especialidad;
    private String fecha;
    private String hora;
    private String motivo;

    public CitaMedica() {
    }

    public CitaMedica(long id, String paciente, String especialidad, String fecha, String hora, String motivo) {
        this.id = id;
        this.paciente = paciente;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getEspecialidad() {
        return especialidad;
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

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
