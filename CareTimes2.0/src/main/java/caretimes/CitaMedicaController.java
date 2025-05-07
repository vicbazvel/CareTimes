package citas;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.*;

@RestController
public class CitaMedicaController {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/dit";
    private static final String USER = "dit";
    private static final String PASS = "dit";

    @PostMapping("/cita")
    public ResponseEntity<String> crearCita(@RequestBody CitaMedica cita) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement st = conn.createStatement()) {

            int result = st.executeUpdate("INSERT INTO citas (paciente, doctor, fecha, hora, motivo) VALUES (" +
                    "'" + cita.getPaciente() + "', '" + cita.getDoctor() + "', '" + cita.getFecha() + "', '" +
                    cita.getHora() + "', '" + cita.getMotivo() + "')");

            return result > 0 ? new ResponseEntity<>("Cita creada", HttpStatus.CREATED)
                              : new ResponseEntity<>("Error al crear", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cita/{id}")
    public ResponseEntity<CitaMedica> obtenerCita(@PathVariable long id) {
        CitaMedica cita = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM citas WHERE id = " + id)) {

            if (rs.next()) {
                cita = new CitaMedica(
                    rs.getLong("id"),
                    rs.getString("paciente"),
                    rs.getString("doctor"),
                    rs.getString("fecha"),
                    rs.getString("hora"),
                    rs.getString("motivo")
                );
                return new ResponseEntity<>(cita, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cita/{id}")
    public ResponseEntity<String> actualizarCita(@PathVariable long id, @RequestBody CitaMedica cita) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement st = conn.createStatement()) {

            int result = st.executeUpdate("UPDATE citas SET " +
                "paciente = '" + cita.getPaciente() + "', " +
                "doctor = '" + cita.getDoctor() + "', " +
                "fecha = '" + cita.getFecha() + "', " +
                "hora = '" + cita.getHora() + "', " +
                "motivo = '" + cita.getMotivo() + "' WHERE id = " + id);

            return result > 0 ? new ResponseEntity<>("Cita actualizada", HttpStatus.OK)
                              : new ResponseEntity<>("No se encontró la cita", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cita/{id}")
    public ResponseEntity<String> eliminarCita(@PathVariable long id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement st = conn.createStatement()) {

            int result = st.executeUpdate("DELETE FROM citas WHERE id = " + id);
            return result > 0 ? new ResponseEntity<>("Cita eliminada", HttpStatus.OK)
                              : new ResponseEntity<>("No encontrada", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

