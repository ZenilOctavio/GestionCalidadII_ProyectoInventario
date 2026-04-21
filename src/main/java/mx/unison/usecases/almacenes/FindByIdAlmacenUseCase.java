package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.Optional;

public class FindByIdAlmacenUseCase {
    private AlmacenesRepository repository;

    public FindByIdAlmacenUseCase(AlmacenesRepository repository) {
        this.repository = repository;
    }

    public Optional<Almacen> execute(int id){
        return repository.findById(id);
    }
}
