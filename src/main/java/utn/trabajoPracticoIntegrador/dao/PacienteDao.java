package utn.trabajoPracticoIntegrador.dao;

import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao implements GenericDao<Paciente> {

    @Override
    public void crear(Paciente entidad) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            crear(entidad, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear paciente.", e);
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

    public void crear(Paciente entidad, Connection conn) throws SQLException {
        String sql = "INSERT INTO paciente (eliminado, nombre, apellido, dni, fechaNacimiento, historia_clinica_id) VALUES ( ?, ?, ?, ?, ?, ? )";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setBoolean(1, false);
            pstmt.setString(2, entidad.getNombre());
            pstmt.setString(3, entidad.getApellido());
            pstmt.setString(4, entidad.getDni());
            pstmt.setDate(5, Date.valueOf(entidad.getFechaNacimiento()));
            if (entidad.getHistoriaClinica() != null) {
                pstmt.setLong(6, entidad.getHistoriaClinica().getId());
            } else {
                pstmt.setNull(6, Types.BIGINT);
            }
            pstmt.executeUpdate();
            try (ResultSet result = pstmt.getGeneratedKeys()) {
                if (result.next()) {
                    entidad.setId(result.getLong(1));
                }
            }
        }
    }

    @Override
    public Paciente leer(long id) {
        String sql = "SELECT " +
                "p.id as p_id, p.eliminado as p_eliminado, p.nombre, p.apellido, p.dni, p.fechaNacimiento, " +
                "h.id as h_id, h.eliminado as h_eliminado, h.nroHistoria, h.grupoSanguineo, h.antecedentes, h.medicacionActual, h.observaciones " +
                "FROM paciente p " +
                "LEFT JOIN historiaclinica h ON p.historia_clinica_id = h.id " +
                "WHERE p.id = ? AND p.eliminado = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    return mapResultSetToPaciente(result); // ¡¡NUEVO!! Usamos un helper
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer paciente.", e);
        }
    }

    // --- LEER TODOS 
    @Override
    public List<Paciente> leerTodos() {
        String sql = "SELECT " +
                "p.id as p_id, p.eliminado as p_eliminado, p.nombre, p.apellido, p.dni, p.fechaNacimiento, " +
                "h.id as h_id, h.eliminado as h_eliminado, h.nroHistoria, h.grupoSanguineo, h.antecedentes, h.medicacionActual, h.observaciones " +
                "FROM paciente p " +
                "LEFT JOIN historiaclinica h ON p.historia_clinica_id = h.id " +
                "WHERE p.eliminado = false";

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                pacientes.add(mapResultSetToPaciente(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer todos los pacientes.", e);
        }
        return pacientes;
    }

    // --- ACTUALIZAR
    @Override
    public void actualizar(Paciente entidad) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            actualizar(entidad, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar paciente.", e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void actualizar(Paciente entidad, Connection conn) throws SQLException {
        String sql = "UPDATE paciente SET nombre = ?, apellido = ?, dni = ?, fechaNacimiento = ?, " +
                "historia_clinica_id = ?, eliminado = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidad.getNombre());
            pstmt.setString(2, entidad.getApellido());
            pstmt.setString(3, entidad.getDni());
            pstmt.setDate(4, Date.valueOf(entidad.getFechaNacimiento()));
            if (entidad.getHistoriaClinica() != null) {
                pstmt.setLong(5, entidad.getHistoriaClinica().getId());
            } else {
                pstmt.setNull(5, Types.BIGINT);
            }
            pstmt.setBoolean(6, entidad.isEliminado());
            pstmt.setLong(7, entidad.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(long id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            eliminar(id, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar paciente.", e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void eliminar(long id, Connection conn) throws SQLException {
        String sql = "UPDATE paciente SET eliminado = true WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    // --- MÉTODO PARA VALIDACIÓN
    public Paciente getByDni(String dni, Connection conn) throws SQLException {
        String sql = "SELECT " +
                "p.id as p_id, p.eliminado as p_eliminado, p.nombre, p.apellido, p.dni, p.fechaNacimiento, " +
                "h.id as h_id, h.eliminado as h_eliminado, h.nroHistoria, h.grupoSanguineo, h.antecedentes, h.medicacionActual, h.observaciones " +
                "FROM paciente p " +
                "LEFT JOIN historiaclinica h ON p.historia_clinica_id = h.id " +
                "WHERE p.dni = ? AND p.eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    return mapResultSetToPaciente(result);
                } else {
                    return null;
                }
            }
        }
    }
    /**
     * MÉTODO SIMPLE: Busca por DNI (para lecturas no transaccionales)
     * Este método abre y cierra su propia conexión.
     * Es llamado por el Servicio cuando no se necesita una transacción.
     */
    public Paciente getByDni(String dni) throws Exception {
        // 1. Abre su propia conexión
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 2. Llama al método transaccional que ya existía
            return getByDni(dni, conn);

        } catch (SQLException e) {
            // 3. Maneja la excepción
            throw new Exception("Error al buscar paciente por DNI: " + e.getMessage(), e);
        }
    }

    // --- MÉTODO HELPER
    private Paciente mapResultSetToPaciente(ResultSet result) throws SQLException {
        // 1. Construir la HistoriaClinica (si existe)
        HistoriaClinica hc = null;
        long historiaId = result.getLong("h_id");
        if (!result.wasNull()) {
            hc = new HistoriaClinica(
                    historiaId,
                    result.getBoolean("h_eliminado"),
                    result.getString("nroHistoria"),
                    GrupoSanguineo.fromValue(result.getString("grupoSanguineo")), // Conversión de String a Enum,
                    result.getString("antecedentes"),
                    result.getString("medicacionActual"),
                    result.getString("observaciones")
            );
        }
        // 2. Construir el Paciente
        return new Paciente(
                result.getLong("p_id"),
                result.getBoolean("p_eliminado"),
                result.getString("nombre"),
                result.getString("apellido"),
                result.getString("dni"),
                result.getDate("fechaNacimiento").toLocalDate(),
                hc
        );
    }
}