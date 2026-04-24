package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.infrastructure.persistence.dao.ProductoDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de ProductsRepository utilizando ORMLite para la persistencia.
 */
public class ORMLiteProductsRepository implements ProductsRepository {
    private final ProductoDAO productoDAO;

    /**
     * Crea una instancia de ORMLiteProductsRepository.
     * @param productoDAO El DAO para la entidad Producto.
     */
    public ORMLiteProductsRepository(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Producto> findAll() {
        try {
            return productoDAO.findAll();
        } catch (SQLException ex) {
            System.err.println("Error al obtener todos los productos: " + ex.getMessage());
            ex.printStackTrace();
            return List.of();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createProduct(Producto producto) {
        try {
            productoDAO.create(producto);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al crear producto: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Producto> findById(int id) {
        try {
            return productoDAO.findById(id);
        } catch (SQLException ex) {
            System.err.println("Error al buscar producto por id: " + ex.getMessage());
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateProduct(Producto producto) {
        try {
            productoDAO.update(producto);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al actualizar producto: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteProduct(Producto producto) {
        try {
            productoDAO.delete(producto);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar producto: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}