package mx.unison.usecases.productos;

import mx.unison.core.domain.repository.ProductsRepository;

public class DeleteProductUseCase {
    private final ProductsRepository repository;

    public DeleteProductUseCase(ProductsRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int id){
        var prodOpt = repository.findById(id);

        if (prodOpt.isEmpty()) return false;

        var producto = prodOpt.get();

        return repository.deleteProduct(producto);
    }
}
