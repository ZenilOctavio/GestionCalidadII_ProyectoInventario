package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.List;

public class GetAllAlmacenesUseCase {
    private final AlmacenesRepository repository;

    public GetAllAlmacenesUseCase(AlmacenesRepository rep) {
        this.repository = rep;
    }

    public List<Almacen> execute(){
        return repository.findAll();
    }
}
