package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Producto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Producto.
 */
public interface ProductsRepository {
    /**
     * Recupera todos los productos registrados en el sistema.
     * @return Una lista de todos los productos.
     */
    List<Producto> findAll();

    /**
     * Registra un nuevo producto en el sistema.
     *
     * @param producto El objeto Producto a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    boolean createProduct(Producto producto);

    /**
     * Busca un producto por su identificador único.
     *
     * @param id El identificador del producto.
     * @return Un Optional que contiene el producto si se encuentra, o vacío si no.
     */
    Optional<Producto> findById(int id);

    /**
     * Actualiza la información de un producto existente.
     *
     * @param producto El objeto Producto con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    boolean updateProduct(Producto producto);

    /**
     * Elimina un producto del sistema.
     *
     * @param producto El objeto Producto a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean deleteProduct(Producto producto);
}
