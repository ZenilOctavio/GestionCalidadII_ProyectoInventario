package mx.unison.usecases.almacenes;

import mx.unison.core.domain.repository.AlmacenesRepository;

/**
 * Caso de uso para la eliminación de un almacén.
 */
public class DeleteAlmacenUseCase {
    private final AlmacenesRepository repository;

    /**
     * Crea una instancia de DeleteAlmacenUseCase.
     * @param repository El repositorio de almacenes.
     */
    public DeleteAlmacenUseCase(AlmacenesRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para eliminar un almacén por su ID.
     *
     * @param id El identificador del almacén a eliminar.
     * @return true si la eliminación fue exitosa, false si no se encontró el almacén o hubo un error.
     */
    public boolean execute(int id){
        var almacenOpt = repository.findById(id);

        if (almacenOpt.isEmpty()) return false;

        var almacen = almacenOpt.get();

        return repository.deleteAlmacen(almacen);
    }
}

