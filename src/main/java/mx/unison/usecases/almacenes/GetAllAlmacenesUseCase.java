package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.List;

/**
 * Caso de uso para obtener la lista de todos los almacenes.
 */
public class GetAllAlmacenesUseCase {
    private final AlmacenesRepository repository;

    /**
     * Crea una instancia de GetAllAlmacenesUseCase.
     * @param rep El repositorio de almacenes.
     */
    public GetAllAlmacenesUseCase(AlmacenesRepository rep) {
        this.repository = rep;
    }

    /**
     * Ejecuta la recuperación de todos los almacenes.
     * @return Una lista de almacenes.
     */
    public List<Almacen> execute(){
        return repository.findAll();
    }
}
