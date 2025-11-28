package utn.trabajoPracticoIntegrador.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import utn.trabajoPracticoIntegrador.config.DatabaseConnection;
import utn.trabajoPracticoIntegrador.dao.HistoriaClinicaDao;
import utn.trabajoPracticoIntegrador.dao.PacienteDao;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;


public class PacienteService implements GenericService<Paciente> {

    private final PacienteDao pacienteDao;
    private final HistoriaClinicaService historiaClinicaService;
    private final HistoriaClinicaDao historiaClinicaDao;

    // Constructor para Inyección de Dependencias
    public PacienteService(PacienteDao pacienteDao, HistoriaClinicaService historiaService, HistoriaClinicaDao historiaDao) {
        if (pacienteDao == null || historiaService == null || historiaDao == null) {
            throw new IllegalArgumentException("Las dependencias no pueden ser null");
        }
        this.pacienteDao = pacienteDao;
        this.historiaClinicaService = historiaService;
        this.historiaClinicaDao = historiaDao;
    }
    
    public HistoriaClinicaService getHistoriaClinicaService() {
        return this.historiaClinicaService;
    }

    @Override
    public void insertar(Paciente paciente) throws Exception {
        validatePaciente(paciente);
        
        HistoriaClinica historia = paciente.getHistoriaClinica();
        if (historia == null) {
            throw new IllegalArgumentException("El paciente debe tener una historia clinica para ser insertado");
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Validación de unicidad dentro de la transaccion
            validateDniUnique(paciente.getDni(), null, conn);
            if (historiaClinicaDao.getByNroHistoria(historia.getNroHistoria(), conn)!= null) {
                throw new IllegalArgumentException("Error: Ya existe una historia clínica con el Nro " + historia.getNroHistoria());
            }
            //Creamos La hisotira clinica
            historiaClinicaDao.crear(historia,conn);
            //Creamos el paciente
            pacienteDao.crear(paciente, conn); 

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            System.out.println("Rollback ejecutado correctamente.");
            throw new SQLException("Error al insertar paciente : " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    @Override
    public void actualizar(Paciente paciente) throws SQLException {
        validatePaciente(paciente);
        if (paciente.getId() == null || paciente.getId()<=0) {
            throw new IllegalArgumentException("El id debe ser mayor a 0 para actualizar");
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            validateDniUnique(paciente.getDni(),paciente.getId(), conn);
            
            if (paciente.getHistoriaClinica() != null) {
                historiaClinicaDao.actualizar(paciente.getHistoriaClinica(), conn);
            }
            pacienteDao.actualizar(paciente, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al actualizar paciente", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        if (id<=0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            pacienteDao.eliminar(id, conn);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Error al eliminar paciente", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public void eliminarHistorialDePaciente (long pacienteId,long historiaId) throws Exception{
        if (pacienteId<= 0 || historiaId <= 0) {
            throw new IllegalArgumentException("Los ID deben ser mayor a 0");
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Paciente paciente = pacienteDao.leer(pacienteId); 
            if (paciente == null) {
                throw new IllegalArgumentException("Paciente no encontrado con ID: " + pacienteId);
            }
            if (paciente.getHistoriaClinica() == null || paciente.getHistoriaClinica().getId() != historiaId) {
                throw new IllegalArgumentException("La historia clínica no pertenece a este paciente");
            }
            // 1. Desasociar (actualizamos FK a NULL)
            paciente.setHistoriaClinica(null);
            pacienteDao.actualizar(paciente, conn);
            // 2. Eliminar Historia
            historiaClinicaDao.eliminar(historiaId, conn);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error en eliminación segura de Historia: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public Paciente getById(long id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser > 0");
        }
        return pacienteDao.leer(id);
    }
    
    @Override
    public List<Paciente> getAll() throws Exception {
        return pacienteDao.leerTodos();
    }
    
    public Paciente getByDni(String dni) throws Exception {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }
        return pacienteDao.getByDni(dni); 
    }
    
    private void validatePaciente(Paciente paciente) {
        //Validación de atributos del paciente
        if (paciente == null) {
            throw new IllegalArgumentException("Entidad paciente vacia.");
        }
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente no puede estar vacio.");
        }
        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del paciente no puede estar vacio.");
        }
        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacio.");
        }
    }

    private void validateDniUnique(String dni, Long pacienteId, Connection conn) throws Exception { 
        Paciente existente = pacienteDao.getByDni(dni, conn); 
        if (existente != null) {
            // Chequeo de IDs con Long y a prueba de null
            if (!java.util.Objects.equals(existente.getId(), pacienteId)) {
                throw new IllegalArgumentException("Ya existe un paciente con el DNI: " + dni);
            }
        }
    }
}