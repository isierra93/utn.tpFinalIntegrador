package utn.trabajoPracticoIntegrador.dao;

import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.entities.GrupoSanguineo;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinicaDao implements GenericDao<HistoriaClinica>{

    @Override
    public void crear(HistoriaClinica entidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO historiaclinica (eliminado, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones)" +
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
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM historiaclinica WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, id);

                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()){
                        boolean eliminado = result.getBoolean("eliminado");
                        String nroHistoria = result.getString("nroHistoria");
                        GrupoSanguineo grupoSanguineo = GrupoSanguineo.buscarPorValor(result.getString("grupoSanguineo"));
                        String antecedentes = result.getString("antecedentes");
                        String medicacionActual = result.getString("medicacionActual");
                        String observaciones = result.getString("observaciones");

                        return new HistoriaClinica(id, eliminado, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer historia clinica.", e);
        }
    }

    @Override
    public List<HistoriaClinica> leerTodos() {
        List<HistoriaClinica> listaDeHistoriasClinicas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM historiaclinica";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet result = pstmt.executeQuery()){
                    while (result.next()){
                        Long id = result.getLong("id");
                        boolean eliminado = result.getBoolean("eliminado");
                        String nroHistoria = result.getString("nroHistoria");
                        GrupoSanguineo grupoSanguineo = GrupoSanguineo.buscarPorValor(result.getString("grupoSanguineo"));
                        String antecedentes = result.getString("antecedentes");
                        String medicacionActual = result.getString("medicacionActual");
                        String observaciones = result.getString("observaciones");

                        listaDeHistoriasClinicas.add(new HistoriaClinica(id, eliminado, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer todas las historias clinicas.", e);
        }
        return listaDeHistoriasClinicas;
    }

    @Override
    public void actualizar(HistoriaClinica entidad) {
        try ( Connection conn = DatabaseConnection.getConnection() ) {
            String sql = "UPDATE historiaclinica SET eliminado = ?," +
                    " nroHistoria = ?, " +
                    "grupoSanguineo = ?, " +
                    "antecedentes = ?, " +
                    "medicacionActual = ?, " +
                    "observaciones = ? " +
                    "WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setBoolean(1, entidad.isEliminado());
                pstmt.setString(2, entidad.getNroHistoria());
                pstmt.setString(3,entidad.getGrupoSanguineo().getGrupoSanguineo());
                pstmt.setString(4,entidad.getAntecedentes());
                pstmt.setString(5,entidad.getMedicacionActual());
                pstmt.setString(6,entidad.getObservaciones());
                pstmt.setLong(7,entidad.getId());

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar historia clinica.", e);
        }
    }

    @Override
    public void eliminar(long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE historiaclinica SET eliminado = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setBoolean(1, true);
                pstmt.setLong(2, id);

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar historia clinica.", e);
        }
    }
}
