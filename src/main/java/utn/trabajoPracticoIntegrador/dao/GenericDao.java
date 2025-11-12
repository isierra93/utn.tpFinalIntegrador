package utn.trabajoPracticoIntegrador.dao;

import java.util.List;

public interface GenericDao<T> {

    /**
     * @param entidad
     * Guarda una nueva entidad en la base de datos.
     */
    void crear(T entidad);

    /**
     * Busca y devuelve una entidad por su ID.
     * @return El objeto T encontrado, o null si no existe o esta eliminada
     */
    T leer(long id);

    /**
     * Devuelve una lista con todas las entidades (no eliminadas).
     */
    List<T> leerTodos();

    /**
     * Actualiza los datos de una entidad existente.
     */
    void actualizar(T entidad);

    /**
     * Realiza una baja lógica de la entidad (eliminado = true).
     */
    void eliminar(long id);
}
