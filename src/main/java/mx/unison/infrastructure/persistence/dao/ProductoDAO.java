package mx.unison.infrastructure.persistence.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.core.domain.models.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Producto utilizando ORMLite.
 */
public class ProductoDAO {
    private final Dao<Producto, Integer> dao;

    /**
     * Crea una instancia de ProductoDAO.
     * @param dao El objeto DAO de ORMLite.
     */
    public ProductoDAO(Dao<Producto, Integer> dao) {
        this.dao = dao;
    }

    /**
     * Recupera todos los productos.
     * @return Una lista de productos.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public List<Producto> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id El identificador del producto.
     * @return Un Optional con el producto.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public Optional<Producto> findById(int id) throws SQLException {
        Producto producto = dao.queryForId(id);
        return Optional.ofNullable(producto);
    }

    /**
     * Crea un nuevo registro de producto.
     * @param producto El producto a crear.
     * @throws SQLException Si ocurre un error en la inserción.
     */
    public void create(Producto producto) throws SQLException {
        dao.create(producto);
    }

    /**
     * Actualiza un registro de producto.
     * @param producto El producto con los cambios.
     * @throws SQLException Si ocurre un error en la actualización.
     */
    public void update(Producto producto) throws SQLException {
        dao.update(producto);
    }

    /**
     * Elimina un registro de producto.
     * @param producto El producto a eliminar.
     * @throws SQLException Si ocurre un error en la eliminación.
     */
    public void delete(Producto producto) throws SQLException {
        dao.delete(producto);
    }

    /**
     * Elimina un producto por su ID.
     * @param id El identificador del producto.
     * @throws SQLException Si ocurre un error en la eliminación.
     */
    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}