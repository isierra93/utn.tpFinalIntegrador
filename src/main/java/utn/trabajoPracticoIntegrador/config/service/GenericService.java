/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utn.trabajoPracticoIntegrador.config.service;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cristian
 */
public interface GenericService<T> {

    void insertar(T entity) throws SQLException;

    void actualizar(T entity) throws SQLException;

    void eliminar(long id) throws SQLException;

    T getById(long id) throws SQLException;

    List<T> getAll() throws SQLException;
}
