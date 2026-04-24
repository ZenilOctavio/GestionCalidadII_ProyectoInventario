package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.List;

/**
 * Caso de uso para obtener la lista de todos los productos.
 */
public class GetAllProductosUseCase {
    private final ProductsRepository repository;

    /**
     * Crea una instancia de GetAllProductosUseCase.
     * @param rep El repositorio de productos.
     */
    public GetAllProductosUseCase(ProductsRepository rep) {
        this.repository = rep;
    }

    /**
     * Ejecuta la recuperación de todos los productos.
     * @return Una lista de productos.
     */
    public List<Producto> execute(){
        return repository.findAll();
    }
}
