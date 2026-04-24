package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Almacen;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Almacen.
 */
public interface AlmacenesRepository {
    /**
     * Recupera todos los almacenes registrados en el sistema.
     * @return Una lista de todos los almacenes.
     */
    List<Almacen> findAll();

    /**
     * Registra un nuevo almacén en el sistema.
     *
     * @param almacen El objeto Almacen a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    boolean createAlmacen(Almacen almacen);

    /**
     * Busca un almacén por su identificador único.
     *
     * @param id El identificador del almacén.
     * @return Un Optional que contiene el almacén si se encuentra, o vacío si no.
     */
    Optional<Almacen> findById(int id);

    /**
     * Actualiza la información de un almacén existente.
     *
     * @param almacen El objeto Almacen con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    boolean updateAlmacen(Almacen almacen);

    /**
     * Elimina un almacén del sistema.
     *
     * @param almacen El objeto Almacen a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean deleteAlmacen(Almacen almacen);
}
