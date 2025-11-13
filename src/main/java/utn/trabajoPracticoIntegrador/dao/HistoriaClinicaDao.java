
package utn.trabajoPracticoIntegrador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;

/**
 *
 * @author Cristian
 */
public class HistoriaClinicaDao implements GenericDao<HistoriaClinica> {

    // --- MÉTODO 1: Para la interfaz (operación simple) ---
    @Override
    public void crear(HistoriaClinica entidad) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            crear(entidad, conn); // Llama al método transaccional
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear historia clínica.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void crear(HistoriaClinica entidad, Connection conn) throws SQLException {
        String sql = "INSERT INTO historiaclinica (eliminado, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setBoolean(1, false);
            pstmt.setString(2, entidad.getNroHistoria());
            
            // --- Conversión de Enum a String ---
            pstmt.setString(3, entidad.getGrupoSanguineo().getValorDb());
            
            pstmt.setString(4, entidad.getAntecedentes());
            pstmt.setString(5, entidad.getMedicacionActual());
            pstmt.setString(6, entidad.getObservaciones());
            
            pstmt.executeUpdate();
            // Obtenemos el ID que generó la DB
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // Seteamos el ID en el objeto que nos pasaron por parámetro
                    entidad.setId(rs.getLong(1)); 
                }
            }
        }
    }

    // --- MÉTODO DE LECTURA (con JOIN en PacienteDao) ---
    // Este método es simple. Lee solo la historia.
    // El 'leer' de PacienteDao será el que haga el JOIN.
    @Override
    public HistoriaClinica leer(long id) {
        String sql = "SELECT * FROM historiaclinica WHERE id = ? AND eliminado = false";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Usamos un método helper para no repetir código
                    return mapResultSetToHistoria(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer historia clínica.", e);
        }
    }

    @Override
    public List<HistoriaClinica> leerTodos() {
        String sql = "SELECT * FROM historiaclinica WHERE eliminado = false";
        List<HistoriaClinica> historias = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                historias.add(mapResultSetToHistoria(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer todas las historias clínicas.", e);
        }
        return historias;
    }

    // --- ACTUALIZAR (con los dos métodos) ---

    @Override
    public void actualizar(HistoriaClinica entidad) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            actualizar(entidad, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar historia clínica.", e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void actualizar(HistoriaClinica entidad, Connection conn) throws SQLException {
        String sql = "UPDATE historiaclinica SET nroHistoria = ?, grupoSanguineo = ?, " +
                     "antecedentes = ?, medicacionActual = ?, observaciones = ?, eliminado = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidad.getNroHistoria());
            pstmt.setString(2, entidad.getGrupoSanguineo().getValorDb()); // Enum a String
            pstmt.setString(3, entidad.getAntecedentes());
            pstmt.setString(4, entidad.getMedicacionActual());
            pstmt.setString(5, entidad.getObservaciones());
            pstmt.setBoolean(6, entidad.isEliminado());
            pstmt.setLong(7, entidad.getId());

            pstmt.executeUpdate();
        }
    }

    // --- ELIMINAR (Lógico, con los dos métodos) ---

    @Override
    public void eliminar(long id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            eliminar(id, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar historia clínica.", e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void eliminar(long id, Connection conn) throws SQLException {
        String sql = "UPDATE historiaclinica SET eliminado = true WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private HistoriaClinica mapResultSetToHistoria(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        boolean eliminado = rs.getBoolean("eliminado");
        String nroHistoria = rs.getString("nroHistoria");
        
        // --- Conversión de String a Enum
        GrupoSanguineo gs = GrupoSanguineo.fromValue(rs.getString("grupoSanguineo"));
        
        String antecedentes = rs.getString("antecedentes");
        String medicacionActual = rs.getString("medicacionActual");
        String observaciones = rs.getString("observaciones");

        return new HistoriaClinica(id, eliminado, nroHistoria, gs, antecedentes, medicacionActual, observaciones);
    }
    public HistoriaClinica getByNroHistoria(String nroHistoria, Connection conn) throws SQLException {
        String sql = "SELECT * FROM historiaclinica WHERE nroHistoria = ? AND eliminado = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nroHistoria);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHistoria(rs);
                } else {
                    return null;
                }
            }
        }
    }
}
