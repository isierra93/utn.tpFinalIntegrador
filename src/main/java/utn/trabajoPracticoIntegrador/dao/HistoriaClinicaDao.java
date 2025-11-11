package utn.trabajoPracticoIntegrador.dao;

import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;

import java.sql.*;
import java.util.List;

public class HistoriaClinicaDao implements GenericDao<HistoriaClinica>{

    @Override
    public void crear(HistoriaClinica entidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO historiaClinica (eliminado, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)" +
                    "VALUES ( ? , ? , ? , ?, ?, ? )";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setBoolean(1, false);
                pstmt.setString(2, entidad.getNroHistoria());
                pstmt.setString(3, entidad.getGrupoSanguineo().getGrupoSanguineo());
                pstmt.setString(4, entidad.getAntecedentes());
                pstmt.setString(5, entidad.getMedicacionActual());
                pstmt.setString(6, entidad.getObservaciones());
                pstmt.executeUpdate();

                try (ResultSet result = pstmt.getGeneratedKeys()) {
                    if (result.next()){
                        Long id = result.getLong("id");
                        entidad.setId(id);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear historia clinica.", e);
        }
    }

    @Override
    public HistoriaClinica leer(long id) {
        return null;
    }

    @Override
    public List<HistoriaClinica> leerTodos() {
        return List.of();
    }

    @Override
    public void actualizar(HistoriaClinica entidad) {

    }

    @Override
    public void eliminar(long id) {

    }
}
