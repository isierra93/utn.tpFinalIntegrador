/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utn.trabajoPracticoIntegrador.service;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cristian
 */
public interface GenericService<T> {

    void insertar(T entity) throws Exception;

    void actualizar(T entity) throws Exception;

    void eliminar(long id) throws Exception;

    T getById(long id) throws Exception;

    List<T> getAll() throws Exception;
}
