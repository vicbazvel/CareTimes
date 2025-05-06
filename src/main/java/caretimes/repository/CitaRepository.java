package com.caretimes.repository;

import com.caretimes.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Consultas personalizadas
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByMedicoIdAndEstado(Long medicoId, String estado);
}
