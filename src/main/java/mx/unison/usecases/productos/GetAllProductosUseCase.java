package mx.unison.usecases.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.List;

public class GetAllProductosUseCase {
    private final ProductsRepository repository;

    public GetAllProductosUseCase(ProductsRepository rep) {
        this.repository = rep;
    }

    public List<Producto> execute(){
        return repository.findAll();
    }
}
