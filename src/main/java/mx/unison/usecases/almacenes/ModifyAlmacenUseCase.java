package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;

/**
 * Caso de uso para modificar la información de un almacén existente.
 */
public class ModifyAlmacenUseCase {
    private AlmacenesRepository repository;

    /**
     * Crea una instancia de ModifyAlmacenUseCase.
     * @param repository El repositorio de almacenes.
     */
    public ModifyAlmacenUseCase(AlmacenesRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para actualizar un almacén.
     *
     * @param id Identificador del almacén a modificar.
     * @param nombre Nuevo nombre del almacén.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean execute(int id, String nombre){
        long timeStamp = System.currentTimeMillis();

        var nuevoAlmacen = new Almacen();
        nuevoAlmacen.setId(id);
        nuevoAlmacen.setNombre(nombre);
        nuevoAlmacen.setFechaHoraUltimaMod(String.valueOf(timeStamp));
        nuevoAlmacen.setUltimoUsuario("ADMIN");

        return repository.updateAlmacen(nuevoAlmacen);
    }
}