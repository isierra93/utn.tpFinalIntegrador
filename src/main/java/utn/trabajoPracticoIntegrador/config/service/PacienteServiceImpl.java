/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.config.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.dao.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.dao.PacienteDao;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;

/**
 *
 * @author Cristian
 */
public class PacienteServiceImpl implements GenericService<Paciente> {

    private PacienteDao pacienteDao;
    private HistoriaClinicaDao historiaClinicaDao;

    public PacienteServiceImpl() {
        this.pacienteDao = new PacienteDao();
        this.historiaClinicaDao = new HistoriaClinicaDao();
    }

    /**
     * Operación compuesta (A y B) con transacción y validación.
     */
    public void crearPacienteYSuHistoria(Paciente paciente, HistoriaClinica historia) throws SQLException {
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            // 3. Validaciones
            if (paciente.getDni() == null || paciente.getDni().isBlank()) {
                throw new RuntimeException("El DNI es obligatorio.");
            }
            if (historia.getNroHistoria() == null || historia.getNroHistoria().isBlank()) {
                throw new RuntimeException("El Nro. de Historia es obligatorio.");
            }

            // Validación de duplicados (Regla 1-a-1)
            if (pacienteDao.getByDni(paciente.getDni(), conn) != null) {
                throw new RuntimeException("Error: Ya existe un paciente con el DNI " + paciente.getDni());
            }
            if (historiaClinicaDao.getByNroHistoria(historia.getNroHistoria(), conn) != null) {
                throw new RuntimeException("Error: Ya existe una historia clínica con el Nro " + historia.getNroHistoria());
            }

            // 4. Ejecutar operaciones compuestas
            // Primero creamos la Historia Clínica (B)
            historiaClinicaDao.crear(historia, conn);
            
            // Le pasamos el objeto 'historia' completo, no el ID.
            paciente.setHistoriaClinica(historia); 
            // -----------------------------
            // Finalmente creamos el Paciente (A)
            pacienteDao.crear(paciente, conn);

            // 5. commit() si todo salió bien
            conn.commit();
            
        } catch (Exception e) {
            // 6. rollback() si algo falló
            if (conn != null) {
                System.err.println("Error en la transacción. Haciendo rollback...");
                conn.rollback();
            }
            // Relanzamos la excepción
            throw new SQLException("Error al crear paciente con historia: " + e.getMessage(), e);
            
        } finally {
            // 7. Restablecer autoCommit(true) y cerrar recursos
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
    public void insertar(Paciente paciente) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Validación: No se puede insertar un paciente sin historia
            // (Esta es una regla de negocio de ejemplo, puedes quitarla si no aplica)
            if (paciente.getHistoriaClinica() == null) {
                 throw new RuntimeException("No se puede crear un paciente sin una Historia Clínica asociada.");
            }
            
            // Validación DNI
            if (pacienteDao.getByDni(paciente.getDni(), conn) != null) {
                throw new RuntimeException("Error: Ya existe un paciente con el DNI " + paciente.getDni());
            }

            pacienteDao.crear(paciente, conn); 

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al insertar paciente: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    @Override
    public void actualizar(Paciente paciente) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // aca faltan validaciones antes de actualizar


            pacienteDao.actualizar(paciente, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al actualizar paciente", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void eliminar(long id) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // aca se pueden hacer validaciones (ej. no borrar si tiene deudas, etc.)

            pacienteDao.eliminar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al eliminar paciente", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    @Override
    public Paciente getById(long id) throws SQLException {
        // Las lecturas no necesitan transacción (generalmente)
        return pacienteDao.leer(id); 
    }
    
    @Override
    public List<Paciente> getAll() throws SQLException {
        return pacienteDao.leerTodos();
    }
}