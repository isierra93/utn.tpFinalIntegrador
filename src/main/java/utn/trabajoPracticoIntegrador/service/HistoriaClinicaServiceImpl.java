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

    private HistoriaClinicaDao historiaClinicaDao;

    public HistoriaClinicaServiceImpl() {
        this.historiaClinicaDao = new HistoriaClinicaDao();
    }

    /**
     * Inserta una nueva HistoriaClinica con validación y transacción.
     */
    @Override
    public void insertar(HistoriaClinica historia) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia transacción

            // 3. Validaciones (campos obligatorios y duplicados)
            if (historia.getNroHistoria() == null || historia.getNroHistoria().isBlank()) {
                throw new RuntimeException("El Nro. de Historia es obligatorio.");
            }
            // Llama al método de validación del DAO (que creamos antes)
            if (historiaClinicaDao.getByNroHistoria(historia.getNroHistoria(), conn) != null) {
                throw new RuntimeException("Error: Ya existe una historia clínica con el Nro " + historia.getNroHistoria());
            }

            // 4. Operación (le pasamos la conexión)
            historiaClinicaDao.crear(historia, conn); 

            // 5. Commit
            conn.commit();
            
        } catch (Exception e) {
            // 6. Rollback
            if (conn != null) conn.rollback();
            // Propagamos la excepción
            throw new SQLException("Error al insertar HistoriaClinica: " + e.getMessage(), e);
        } finally {
            // 7. Restablecer y cerrar
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

    /**
     * Actualiza una HistoriaClinica con transacción.
     */
    @Override
    public void actualizar(HistoriaClinica historia) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // aca faltan validaciones antes de actualizar (ej. verificar que el ID exista)

            historiaClinicaDao.actualizar(historia, conn); // Le pasamos la conexión

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al actualizar HistoriaClinica", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Elimina (baja lógica) una HistoriaClinica con transacción.
     */
    @Override
    public void eliminar(long id) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Validación de regla de negocio: No se debería poder eliminar una HC
            // si está asociada a un paciente. (Esto requeriría un método en PacienteDao
            // como 'findByHistoriaId'). Por ahora, lo dejamos simple.
            
            historiaClinicaDao.eliminar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al eliminar HistoriaClinica", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Obtiene una HistoriaClinica por ID (no necesita transacción).
     */
    @Override
    public HistoriaClinica getById(long id) throws SQLException {
        // Las lecturas simples no necesitan transacción
        return historiaClinicaDao.leer(id);
    }

    /**
     * Obtiene todas las HistoriasClinicas (no necesita transacción).
     */
    @Override
    public List<HistoriaClinica> getAll() throws SQLException {
        // Las lecturas simples no necesitan transacción
        return historiaClinicaDao.leerTodos();
    }
}
