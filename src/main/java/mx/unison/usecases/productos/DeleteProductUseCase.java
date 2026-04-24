package mx.unison.usecases.productos;

import mx.unison.core.domain.repository.ProductsRepository;

/**
 * Caso de uso para la eliminación de un producto.
 */
public class DeleteProductUseCase {
    private final ProductsRepository repository;

    /**
     * Crea una instancia de DeleteProductUseCase.
     * @param repository El repositorio de productos.
     */
    public DeleteProductUseCase(ProductsRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para eliminar un producto por su ID.
     *
     * @param id Identificador del producto a eliminar.
     * @return true si la eliminación fue exitosa, false si no se encontró el producto o hubo un error.
     */
    public boolean execute(int id){
        var prodOpt = repository.findById(id);

        if (prodOpt.isEmpty()) return false;

        var producto = prodOpt.get();

        return repository.deleteProduct(producto);
    }
}
