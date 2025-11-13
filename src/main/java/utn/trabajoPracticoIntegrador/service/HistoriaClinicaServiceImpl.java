/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.dao.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;

/**
 *
 * @author Cristian
 */
public class HistoriaClinicaServiceImpl implements GenericService<HistoriaClinica> {

    private final HistoriaClinicaDao historiaClinicaDao;

    public HistoriaClinicaServiceImpl(HistoriaClinicaDao historiaClinicaDao) {
        if (historiaClinicaDao == null) {
            throw new IllegalArgumentException("HistoriaClinicaDao no puede ser null");
        }
        this.historiaClinicaDao = historiaClinicaDao;
    }

    /**
     * Inserta una nueva HistoriaClinica con validación y transacción.
     */
    @Override
    public void insertar(HistoriaClinica historia) throws Exception {
        validateHistoria(historia);
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia transacción

            if (historiaClinicaDao.getByNroHistoria(historia.getNroHistoria(), conn) != null) {
                throw new IllegalArgumentException("Error: Ya existe una historia clínica con el Nro " + historia.getNroHistoria());
            }

            historiaClinicaDao.crear(historia, conn); 
            conn.commit();
            
        } catch (Exception e) {
            // 6. Rollback
            if (conn != null) conn.rollback();
            throw new Exception("Error al insertar HistoriaClinica: " + e.getMessage(), e);
        } finally {
            // 7. Restablecer y cerramos conexion
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                }
            }
        }
    }
    
    @Override
    public void actualizar(HistoriaClinica historia) throws Exception {
        validateHistoria(historia);
        if (historia.getId() == null || historia.getId() <= 0) { // Chequeo de Long
            throw new IllegalArgumentException("El ID del domicilio debe ser mayor a 0 para actualizar");
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            historiaClinicaDao.actualizar(historia, conn); // Le pasamos la historia y conexión
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al actualizar HistoriaClinica", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            historiaClinicaDao.eliminar(id, conn);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al eliminar HistoriaClinica", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    // este no necesita transaccion
    @Override   
    public HistoriaClinica getById(long id) throws Exception {
        // Las lecturas simples no necesitan transacción
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return historiaClinicaDao.leer(id);
    }

    /**
     * Obtiene todas las HistoriasClinicas tampoco necesita transacción
     */
    @Override
    public List<HistoriaClinica> getAll() throws SQLException {
        return historiaClinicaDao.leerTodos();
    }
    
    private void validateHistoria(HistoriaClinica historia) {
        if (historia == null) {
            throw new IllegalArgumentException("La HistoriaClinica no puede ser null");
        }
        if (historia.getNroHistoria() == null || historia.getNroHistoria().trim().isEmpty()) {
            throw new IllegalArgumentException("El Nro. de Historia no puede estar vacío");
        }
        if (historia.getGrupoSanguineo() == null) {
            throw new IllegalArgumentException("El Grupo Sanguíneo no puede ser null");
        }
    }
}
