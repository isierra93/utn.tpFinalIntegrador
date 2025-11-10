/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.DAO;

/**
 *
 * @author nidia
 */

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T crear(T t, Connection conn) throws Exception;
    //para manejar ausencia de resultados.
    Optional<T> leer(Long id, Connection conn) throws Exception;
    List<T> leerTodos(Connection conn) throws Exception;
    boolean actualizar(T t, Connection conn) throws Exception;
    boolean eliminar(Long id, Connection conn) throws Exception;
}
