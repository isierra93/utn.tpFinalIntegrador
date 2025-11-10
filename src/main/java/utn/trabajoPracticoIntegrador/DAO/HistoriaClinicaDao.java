/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.DAO;

/**
 *
 * @author nidia
 */
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoriaClinicaDao implements GenericDao<HistoriaClinica> {
    
    // ID del paciente asociado, no el ID de la historia clínica. Esto se debe manejar correctamente desde el servicio cuando se cree o actualice la historia.

    @Override
    public HistoriaClinica crear(HistoriaClinica hc, Connection conn) throws SQLException {
        String sql = "INSERT INTO historia_clinica (nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id, eliminado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hc.getNroHistoria());
            ps.setString(2, hc.getGrupoSanguineo().getGrupoSanguineo());
            ps.setString(3, hc.getAntecedentes());
            ps.setString(4, hc.getMedicacionActual());
            ps.setString(5, hc.getObservaciones());
            ps.setLong(6, hc.getId());  // Aquí se debe pasar el paciente_id, pero atención, esto se debe manejar desde el servicio
            ps.setBoolean(7, hc.isEliminado());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Crear historia clinica falló, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hc.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Crear historia clinica falló, no se obtuvo ID.");
                }
            }
        }
        return hc;
    }

    @Override
    public Optional<HistoriaClinica> leer(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM historia_clinica WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<HistoriaClinica> leerTodos(Connection conn) throws SQLException {
        List<HistoriaClinica> lista = new ArrayList<>();
        String sql = "SELECT * FROM historia_clinica WHERE eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }
// ID del paciente asociado, no el ID de la historia clínica. Esto se debe manejar correctamente desde el servicio cuando se cree o actualice la historia.
    
    @Override
    public boolean actualizar(HistoriaClinica hc, Connection conn) throws SQLException {
        String sql = "UPDATE historia_clinica SET nro_historia = ?, grupo_sanguineo = ?, antecedentes = ?, medicacion_actual = ?, observaciones = ?, paciente_id = ?, eliminado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hc.getNroHistoria());
            ps.setString(2, hc.getGrupoSanguineo().getGrupoSanguineo());
            ps.setString(3, hc.getAntecedentes());
            ps.setString(4, hc.getMedicacionActual());
            ps.setString(5, hc.getObservaciones());
            ps.setLong(6, hc.getId());  // Paciente_id, debe manejarse correctamente
            ps.setBoolean(7, hc.isEliminado());
            ps.setLong(8, hc.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean eliminar(Long id, Connection conn) throws SQLException {
        // Eliminación lógica: set eliminado = true
        String sql = "UPDATE historia_clinica SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
// convierte la cadena grupo_sanguineo en el enum GrupoSanguineo
    HistoriaClinica mapear(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nroHistoria = rs.getString("nro_historia");
        GrupoSanguineo grupo = GrupoSanguineo.valueOf(rs.getString("grupo_sanguineo").replace("+", "_POSITIVO").replace("-", "_NEGATIVO"));
        String antecedentes = rs.getString("antecedentes");
        String medicacion = rs.getString("medicacion_actual");
        String observaciones = rs.getString("observaciones");
        boolean eliminado = rs.getBoolean("eliminado");

        return new HistoriaClinica(id, eliminado, nroHistoria, grupo, antecedentes, medicacion, observaciones);
    }
}