package com.caretimes.model;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String especialidad;
    
    // Relación inversa
    @OneToMany(mappedBy = "medico")
    private List<Cita> citas;

	// En Medico.java
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getEspecialidad() { return especialidad; }
	public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

	public List<Cita> getCitas() { return citas; }
	public void setCitas(List<Cita> citas) { this.citas = citas; }

}
