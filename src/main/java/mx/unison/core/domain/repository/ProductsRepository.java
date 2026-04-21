package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository {
    List<Producto> findAll();
    boolean createProduct(Producto producto);
    Optional<Producto> findById(int id);
    boolean updateProduct(Producto producto);
    boolean deleteProduct(Producto producto);
}
