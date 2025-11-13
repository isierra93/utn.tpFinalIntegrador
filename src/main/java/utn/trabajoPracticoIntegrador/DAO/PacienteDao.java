/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.DAO;

import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteDao implements GenericDao<Paciente> {

    private HistoriaClinicaDao historiaDao = new HistoriaClinicaDao();

    @Override
    public Paciente crear(Paciente paciente, Connection conn) throws SQLException {
        String sql = "INSERT INTO paciente (nombre, apellido, dni, fecha_nacimiento, eliminado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setBoolean(5, paciente.isEliminado());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Crear paciente falló, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Crear paciente falló, no se obtuvo ID.");
                }
            }
        }
        return paciente;
    }

    @Override
    public Optional<Paciente> leer(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = mapear(rs);

                    // Cargar la historia clinica
                    String sqlHistoria = "SELECT * FROM historia_clinica WHERE paciente_id = ? AND eliminado = FALSE";
                    try (PreparedStatement psHistoria = conn.prepareStatement(sqlHistoria)) {
                        psHistoria.setLong(1, paciente.getId());
                        try (ResultSet rsHistoria = psHistoria.executeQuery()) {
                            if (rsHistoria.next()) {
                                paciente.setHistoriaClinica(historiaDao.mapear(rsHistoria));
                            }
                        }
                    }

                    return Optional.of(paciente);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Paciente> leerTodos(Connection conn) throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Paciente paciente = mapear(rs);

                // Cargar historia clinica
                String sqlHistoria = "SELECT * FROM historia_clinica WHERE paciente_id = ? AND eliminado = FALSE";
                try (PreparedStatement psHistoria = conn.prepareStatement(sqlHistoria)) {
                    psHistoria.setLong(1, paciente.getId());
                    try (ResultSet rsHistoria = psHistoria.executeQuery()) {
                        if (rsHistoria.next()) {
                            paciente.setHistoriaClinica(historiaDao.mapear(rsHistoria));
                        }
                    }
                }

                lista.add(paciente);
            }
        }
        return lista;
    }

    @Override
    public boolean actualizar(Paciente paciente, Connection conn) throws SQLException {
        String sql = "UPDATE paciente SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ?, eliminado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setBoolean(5, paciente.isEliminado());
            ps.setLong(6, paciente.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean eliminar(Long id, Connection conn) throws SQLException {
        // Eliminación lógica: set eliminado = true
        String sql = "UPDATE paciente SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String dni = rs.getString("dni");
        LocalDate fechaNacimiento = rs.getDate("fecha_nacimiento").toLocalDate();
        boolean eliminado = rs.getBoolean("eliminado");

        return new Paciente(id, eliminado, nombre, apellido, dni, fechaNacimiento, null);
    }
}


