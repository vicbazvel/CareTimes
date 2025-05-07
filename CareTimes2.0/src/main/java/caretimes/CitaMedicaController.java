package citas;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CitaMedicaController {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/dit";
    private static final String USER = "dit";
    private static final String PASS = "dit";

    // --------- CITAS MÉDICAS ---------

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

    // --------- USUARIOS (Doctores / Pacientes) ---------

    @PostMapping("/usuario")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO usuarios (nombre, password, rol) VALUES (?, ?, ?)")) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());

            int result = ps.executeUpdate();
            return result > 0 ? new ResponseEntity<>("Usuario creado", HttpStatus.CREATED)
                              : new ResponseEntity<>("Error al crear", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorRol(@RequestParam String rol) {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios WHERE rol = ?")) {

            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("password"),
                    rs.getString("rol")
                ));
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM usuarios WHERE nombre = ? AND password = ?")) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getPassword());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ResponseEntity<>("Login exitoso como " + rs.getString("rol"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

