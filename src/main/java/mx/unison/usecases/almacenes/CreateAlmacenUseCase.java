package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;

import mx.unison.core.domain.repository.AlmacenesRepository;


/**
 * Caso de uso para la creación de un nuevo almacén.
 */
public class CreateAlmacenUseCase {
    private AlmacenesRepository repository;

    /**
     * Crea una instancia de CreateAlmacenUseCase.
     * @param repository El repositorio de almacenes.
     */
    public CreateAlmacenUseCase(AlmacenesRepository repository){
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para crear un nuevo almacén.
     *
     * @param nombre El nombre del nuevo almacén.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean execute (String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del almacén es obligatorio");
        }

        long timeStamp = System.currentTimeMillis();
        var nuevoAlmacen = new Almacen();
        nuevoAlmacen.setNombre(nombre);
        nuevoAlmacen.setFechaHoraCreacion(String.valueOf(timeStamp));
        nuevoAlmacen.setFechaHoraUltimaMod(String.valueOf(timeStamp));
        nuevoAlmacen.setUltimoUsuario("ADMIN");

        return repository.createAlmacen(nuevoAlmacen);
    }
}
