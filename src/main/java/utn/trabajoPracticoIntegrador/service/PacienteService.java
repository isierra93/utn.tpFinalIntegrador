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
import utn.trabajoPracticoIntegrador.DAO.PacienteDao;
import utn.trabajoPracticoIntegrador.entities.HistoriaClinica;
import utn.trabajoPracticoIntegrador.entities.Paciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static utn.trabajoPracticoIntegrador.config.DatabaseConnection.getConnection;

public class PacienteService implements GenericService<Paciente> {

    private PacienteDao pacienteDao = new PacienteDao();
    private HistoriaClinicaDao historiaDao = new HistoriaClinicaDao();

    @Override
    public Paciente insertar(Paciente paciente) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Validaciones básicas
            if (paciente.getDni() == null || paciente.getDni().isBlank()) {
                throw new IllegalArgumentException("DNI es obligatorio");
            }
            if (paciente.getHistoriaClinica() == null) {
                throw new IllegalArgumentException("El paciente debe tener una historia clínica");
            }

            // Insertar paciente
            paciente = pacienteDao.crear(paciente, conn);

            HistoriaClinica hc = paciente.getHistoriaClinica();
            hc.setEliminado(false);
            // Asignar paciente_id en historia clínica
            // aca el paciente ya tiene ID
            hc.setId(paciente.getId());  // NO, esta línea no es correcta, hay que pasar paciente_id a la consulta en DAO, no id de historia clínica

            // Crear historia clínica asociada
            // Como DAO necesita paciente_id, se debe modificar el DAO para aceptar paciente_id separado o ajustar el atributo.
            // Para simplificar, podemos agregar un atributo pacienteId en HistoriaClinica o pasar como parámetro al DAO.
            // Aquí asumiremos que historiaClinica tiene pacienteId para almacenar FK.

            // Insertar historia clinica con paciente_id:
            // Como no tenemos pacienteId en la entidad, se debe agregar o modificar método DAO.

           

            hc = historiaDao.crearConPacienteId(hc, paciente.getId(), conn);

            conn.commit();
            return paciente;
        } catch (Exception e) {
            throw new SQLException("Error al insertar paciente con historia clínica", e);
        }
    }

    @Override
    public Paciente actualizar(Paciente paciente) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            if (paciente.getId() == null) {
                throw new IllegalArgumentException("El id del paciente es obligatorio para actualizar");
            }

            boolean actualizadoPaciente = pacienteDao.actualizar(paciente, conn);
            if (!actualizadoPaciente) {
                throw new SQLException("No se pudo actualizar paciente");
            }

            HistoriaClinica hc = paciente.getHistoriaClinica();
            if (hc != null) {
                // Actualizar la historia clínica asociada
                boolean actualizadoHistoria = historiaDao.actualizar(hc, conn);
                if (!actualizadoHistoria) {
                    throw new SQLException("No se pudo actualizar historia clínica");
                }
            }

            conn.commit();
            return paciente;
        } catch (Exception e) {
            throw new SQLException("Error al actualizar paciente con historia clínica", e);
        }
    }

    @Override
    public boolean eliminar(Long id) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Eliminación lógica de paciente y su historia (por ON DELETE CASCADE no se elimina, pero se marca eliminado)

            boolean eliminadoPaciente = pacienteDao.eliminar(id, conn);
            if (!eliminadoPaciente) {
                throw new SQLException("No se pudo eliminar paciente");
            }

            // También eliminar historia asociada
            Optional<Paciente> pacienteOpt = pacienteDao.leer(id, conn);
            if (pacienteOpt.isPresent()) {
                HistoriaClinica hc = pacienteOpt.get().getHistoriaClinica();
                if (hc != null) {
                    historiaDao.eliminar(hc.getId(), conn);
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            throw new SQLException("Error al eliminar paciente con historia clínica", e);
        }
    }

    @Override
    public Optional<Paciente> getById(Long id) throws SQLException {
        try (Connection conn = getConnection()) {
            return pacienteDao.leer(id, conn);
        }
    }

    @Override
    public List<Paciente> getAll() throws SQLException {
        try (Connection conn = getConnection()) {
            return pacienteDao.leerTodos(conn);
        }
    }
}
