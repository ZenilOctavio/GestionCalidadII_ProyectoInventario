package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.core.domain.repository.ProductsRepository;

import java.util.Optional;

/**
 * Caso de uso para buscar un almacén por su identificador único.
 */
public class FindByIdAlmacenUseCase {
    private AlmacenesRepository repository;

    /**
     * Crea una instancia de FindByIdAlmacenUseCase.
     * @param repository El repositorio de almacenes.
     */
    public FindByIdAlmacenUseCase(AlmacenesRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la búsqueda del almacén.
     *
     * @param id El identificador del almacén.
     * @return Un Optional con el almacén si se encuentra.
     */
    public Optional<Almacen> execute(int id){
        return repository.findById(id);
    }
}
