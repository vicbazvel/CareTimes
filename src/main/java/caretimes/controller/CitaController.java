package com.caretimes.controller;

import com.caretimes.model.Cita;
import com.caretimes.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {
    
    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // Obtener todas las citas
    @GetMapping
    public List<Cita> getAllCitas() {
        return citaService.findAll();
    }

    // Crear nueva cita
    @PostMapping
    public ResponseEntity<Cita> createCita(@RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.save(cita));
    }

    // Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<Cita> updateCita(
            @PathVariable Long id, 
            @RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.update(id, cita));
    }

    // Eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
