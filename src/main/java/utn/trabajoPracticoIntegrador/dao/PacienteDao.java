package utn.trabajoPracticoIntegrador.dao;

import utn.trabajoPracticoIntegrador.entities.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class PacienteDao implements GenericDao<Paciente>{
    @Override
    public void crear(Paciente entidad) {
        //Try with resources para preparar la conexion y cerrarla al terminar
        try (Connection conn = utn.trabajoPracticoIntegrador.config.DatabaseConnection.getConnection()) {
                //Crear y ejecutar consulta SQL con PreparedStatement
                String sql = "INSERT INTO paciente (eliminado, nombre, apellido, dni, fechaNacimiento) VALUES ( ?, ?, ?, ?, ? )";
                //Try para preparar el insert SQL
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setBoolean(1, false);
                    pstmt.setString(2, entidad.getNombre());
                    pstmt.setString(3, entidad.getApellido());
                    pstmt.setString(4, entidad.getDni());
                    pstmt.setDate(5, Date.valueOf(entidad.getFechaNacimiento()));
                    pstmt.executeUpdate();

                    //Si se genero una clave, la leemos y asignamos la entidad
                    try (ResultSet result = pstmt.getGeneratedKeys()) {
                        if (result.next()){
                            Long id = result.getLong("id");
                            entidad.setId(id);
                        }
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear paciente." , e);
        }
    }


    @Override
    public Paciente leer(long id) {
        //Try with resources para preparar la conexion y cerrarla al terminar
        try(Connection conn = utn.trabajoPracticoIntegrador.config.DatabaseConnection.getConnection()){
            //Crear y ejecutar consulta SQL con PreparedStatement
            String sql = "SELECT * FROM paciente WHERE id = ?";
            //Try para preparar el insert SQL
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setLong(1, id);
                try(ResultSet result = pstmt.executeQuery()){
                    if (result.next()){
                            boolean eliminado = result.getBoolean("eliminado");
                            String nombre = result.getString("nombre");
                            String apellido = result.getString("apellido");
                            String dni = result.getString("dni");
                            LocalDate fechaNacimiento = result.getDate("fechaNacimiento").toLocalDate();
                        return new Paciente(id, eliminado, nombre, apellido, dni, fechaNacimiento, null );
                    }else {
                        return null;
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Error al leer paciente.", e);
        }
    }

    @Override
    public List<Paciente> leerTodos() {
        return List.of();
    }

    @Override
    public void actualizar(Paciente entidad) {

    }

    @Override
    public void eliminar(long id) {

    }
}
