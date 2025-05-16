package citas;

public class Usuario {
    private long id;
    private String nombre;
    private String password;
    private String rol; // "PACIENTE" o "DOCTOR"

    // Nuevos campos de persona
    private String nombreCompleto;
    private String sexo;
    private double altura;
    private double peso;
    private String nacionalidad;

    public Usuario() {}

    public Usuario(long id, String nombre, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    // Constructor con todos los campos (opcional)
    public Usuario(long id, String nombre, String password, String rol,
                   String nombreCompleto, String sexo, double altura, double peso, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.sexo = sexo;
        this.altura = altura;
        this.peso = peso;
        this.nacionalidad = nacionalidad;
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}

