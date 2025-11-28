package utn.trabajoPracticoIntegrador.service;

import java.util.List;

public interface GenericService<T> {

    void insertar(T entity) throws Exception;

    void actualizar(T entity) throws Exception;

    void eliminar(long id) throws Exception;

    T getById(long id) throws Exception;

    List<T> getAll() throws Exception;
}
