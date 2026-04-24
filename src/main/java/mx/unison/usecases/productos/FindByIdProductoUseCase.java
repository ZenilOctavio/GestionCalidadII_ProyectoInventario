package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.Optional;

/**
 * Caso de uso para buscar un producto por su identificador único.
 */
public class FindByIdProductoUseCase {
    private ProductsRepository repository;

    /**
     * Crea una instancia de FindByIdProductoUseCase.
     * @param repository El repositorio de productos.
     */
    public FindByIdProductoUseCase(ProductsRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la búsqueda del producto.
     *
     * @param id Identificador del producto.
     * @return Un Optional con el producto si se encuentra.
     */
    public Optional<Producto> execute(int id){
        return repository.findById(id);
    }
}
