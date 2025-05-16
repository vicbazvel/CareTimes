package citas;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/citas")
public class CitaMedicaController {

private static final String DB_URL = "jdbc:postgresql://localhost:5432/dit";
private static final String USER = "dit";
private static final String PASS = "dit";

// --------- CITAS MÉDICAS ---------

@PostMapping("")
public ResponseEntity<Map<String, String>> crearCita(@RequestBody CitaMedica cita) {
    Map<String, String> respuesta = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO citas (paciente, especialidad, fecha, hora, motivo) VALUES (?, ?, ?, ?, ?)")) {

        ps.setString(1, cita.getPaciente());
        ps.setString(2, cita.getEspecialidad());
        ps.setString(3, cita.getFecha());
        ps.setString(4, cita.getHora());
        ps.setString(5, cita.getMotivo());

        int result = ps.executeUpdate();

        if (result > 0) {
            respuesta.put("mensaje", "Cita creada");
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } else {
            respuesta.put("mensaje", "Error al crear cita");
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    } catch (Exception e) {
        e.printStackTrace();
        respuesta.put("mensaje", "Error interno");
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


@GetMapping("/paciente")
public ResponseEntity<List<CitaMedica>> obtenerCitasPorPaciente(@RequestParam String paciente) {
    List<CitaMedica> citas = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
        
        String sql = "SELECT * FROM citas WHERE paciente = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, paciente);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CitaMedica cita = new CitaMedica(
                rs.getLong("id"),
                rs.getString("paciente"),
                rs.getString("especialidad"),
                rs.getString("fecha"),
                rs.getString("hora"),
                rs.getString("motivo")
            );
            citas.add(cita);
        }

        return new ResponseEntity<>(citas, HttpStatus.OK);

    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}





@DeleteMapping("/{id}")
public ResponseEntity<Map<String, String>> eliminarCita(@PathVariable long id) {
    Map<String, String> respuesta = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement st = conn.createStatement()) {

        int result = st.executeUpdate("DELETE FROM citas WHERE id = " + id);

        if (result > 0) {
            respuesta.put("mensaje", "Cita eliminada");
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            respuesta.put("mensaje", "No se encontró la cita");
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }

    } catch (Exception e) {
        e.printStackTrace();
        respuesta.put("mensaje", "Error interno");
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// --------- USUARIOS (Doctores / Pacientes) ---------

@PostMapping("/usuario")
public ResponseEntity<Map<String, String>> crearUsuario(@RequestBody Usuario usuario) {
    Map<String, String> respuesta = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
        try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE nombre = ?")) {
            check.setString(1, usuario.getNombre());
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                respuesta.put("mensaje", "El usuario ya está registrado");
                return new ResponseEntity<>(respuesta, HttpStatus.CONFLICT);
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO usuarios (nombre, password, rol) VALUES (?, ?, ?)")) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());

            int result = ps.executeUpdate();
            if (result > 0) {
                respuesta.put("mensaje", "Usuario creado correctamente");
                return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
            } else {
                respuesta.put("mensaje", "Error al crear el usuario");
                return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        respuesta.put("mensaje", "Error interno");
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
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
public ResponseEntity<Map<String, String>> login(@RequestBody Usuario usuario) {
    Map<String, String> respuesta = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM usuarios WHERE nombre = ? AND password = ?")) {

        ps.setString(1, usuario.getNombre());
        ps.setString(2, usuario.getPassword());

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            respuesta.put("mensaje", "Login exitoso");
            respuesta.put("rol", rs.getString("rol"));
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("mensaje", "Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        respuesta.put("mensaje", "Error interno");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}

@PutMapping("/perfil/{nombre}")
public ResponseEntity<Map<String, String>> actualizarPerfil(@PathVariable String nombre, @RequestBody Usuario usuario) {
    Map<String, String> respuesta = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
        // Obtener ID del usuario
        long userId = -1;
        try (PreparedStatement getUser = conn.prepareStatement("SELECT id FROM usuarios WHERE nombre = ?")) {
            getUser.setString(1, nombre);
            ResultSet rs = getUser.executeQuery();
            if (rs.next()) {
                userId = rs.getLong("id");
            } else {
                respuesta.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
        }
	// NUEVO: Actualizar contraseña si fue proporcionada
	if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
   	 try (PreparedStatement updatePass = conn.prepareStatement(
           	 "UPDATE usuarios SET password = ? WHERE id = ?")) {
        	updatePass.setString(1, usuario.getPassword());
        	updatePass.setLong(2, userId);
        	updatePass.executeUpdate();
    	}
	}
        // Verificar si ya existe perfil
        try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM persona WHERE usuario_id = ?")) {
            check.setLong(1, userId);
            ResultSet rs = check.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                try (PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO persona (usuario_id, sexo, altura, peso, nombre_completo, nacionalidad) VALUES (?, ?, ?, ?, ?, ?)")) {
                    insert.setLong(1, userId);
                    insert.setString(2, usuario.getSexo());
                    insert.setObject(3, usuario.getAltura());
                    insert.setObject(4, usuario.getPeso());
                    insert.setString(5, usuario.getNombreCompleto());
                    insert.setString(6, usuario.getNacionalidad());
                    insert.executeUpdate();
                }
            } else {
                try (PreparedStatement update = conn.prepareStatement(
                        "UPDATE persona SET sexo = ?, altura = ?, peso = ?, nombre_completo = ?, nacionalidad = ? WHERE usuario_id = ?")) {
                    update.setString(1, usuario.getSexo());
                    update.setObject(2, usuario.getAltura());
                    update.setObject(3, usuario.getPeso());
                    update.setString(4, usuario.getNombreCompleto());
                    update.setString(5, usuario.getNacionalidad());
                    update.setLong(6, userId);
                    update.executeUpdate();
                }
            }

            respuesta.put("mensaje", "Perfil actualizado correctamente");
            return ResponseEntity.ok(respuesta);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        respuesta.put("mensaje", "Error interno");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}

@GetMapping("/usuario/{nombre}")
public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String nombre) {
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        String sqlUsuario = "SELECT * FROM usuarios WHERE nombre = ?";
        Usuario usuario = null;

        try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long userId = rs.getLong("id");
                String password = rs.getString("password");
                String rol = rs.getString("rol");

                String sqlPersona = "SELECT nombre_completo, sexo, altura, peso, nacionalidad FROM persona WHERE usuario_id = ?";
                try (PreparedStatement psPersona = conn.prepareStatement(sqlPersona)) {
                    psPersona.setLong(1, userId);
                    ResultSet rsPersona = psPersona.executeQuery();

                    String nombreCompleto = null;
                    String sexo = null;
                    double altura = 0;
                    double peso = 0;
                    String nacionalidad = null;

                    if (rsPersona.next()) {
                        nombreCompleto = rsPersona.getString("nombre_completo");
                        sexo = rsPersona.getString("sexo");
                        altura = rsPersona.getDouble("altura");
                        peso = rsPersona.getDouble("peso");
                        nacionalidad = rsPersona.getString("nacionalidad");
                    }

                    usuario = new Usuario(userId, nombre, password, rol,
                            nombreCompleto, sexo, altura, peso, nacionalidad);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);

    } catch (SQLException e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

}

