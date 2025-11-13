/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.service;

/**
 *
 * @author nidia
 */

import utn.trabajoPracticoIntegrador.DAO.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static utn.trabajoPracticoIntegrador.config.DatabaseConnection.getConnection;

public class HistoriaClinicaService implements GenericService<HistoriaClinica> {

    private HistoriaClinicaDao dao = new HistoriaClinicaDao();

    @Override
    public HistoriaClinica insertar(HistoriaClinica hc) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            HistoriaClinica creado = dao.crear(hc, conn);
            conn.commit();
            return creado;
        } catch (Exception e) {
            throw new SQLException("Error al insertar historia clínica", e);
        }
    }

    @Override
    public HistoriaClinica actualizar(HistoriaClinica hc) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            boolean actualizado = dao.actualizar(hc, conn);
            if (!actualizado) throw new SQLException("No se pudo actualizar historia clínica");
            conn.commit();
            return hc;
        } catch (Exception e) {
            throw new SQLException("Error al actualizar historia clínica", e);
        }
    }

    @Override
    public boolean eliminar(Long id) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            boolean eliminado = dao.eliminar(id, conn);
            if (!eliminado) throw new SQLException("No se pudo eliminar historia clínica");
            conn.commit();
            return eliminado;
        } catch (Exception e) {
            throw new SQLException("Error al eliminar historia clínica", e);
        }
    }

    @Override
    public Optional<HistoriaClinica> getById(Long id) throws SQLException {
        try (Connection conn = getConnection()) {
            return dao.leer(id, conn);
        }
    }

    @Override
    public List<HistoriaClinica> getAll() throws SQLException {
        try (Connection conn = getConnection()) {
            return dao.leerTodos(conn);
        }
    }
}