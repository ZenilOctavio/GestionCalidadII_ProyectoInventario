package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.Optional;

public class FindByIdProductoUseCase {
    private ProductsRepository repository;

    public FindByIdProductoUseCase(ProductsRepository repository) {
        this.repository = repository;
    }

    public Optional<Producto> execute(int id){
        return repository.findById(id);
    }
}
