package com.caretimes.service;

import com.caretimes.model.Cita;
import com.caretimes.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaService {
    
    private final CitaRepository repository;

    public CitaService(CitaRepository repository) {
        this.repository = repository;
    }

    public List<Cita> findAll() {
        return repository.findAll();
    }

    public Cita save(Cita cita) {
        return repository.save(cita);
    }

    public Cita update(Long id, Cita cita) {
        cita.setId(id);
        return repository.save(cita);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
