/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utn.trabajoPracticoIntegrador.service;

/**
 *
 * @author nidia
 */
import java.util.List;
import java.util.Optional;

public interface GenericService<T> {
    T insertar(T t) throws Exception;
    T actualizar(T t) throws Exception;
    boolean eliminar(Long id) throws Exception;
    Optional<T> getById(Long id) throws Exception;
    List<T> getAll() throws Exception;
}