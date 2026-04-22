package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;

import mx.unison.core.domain.repository.AlmacenesRepository;


public class CreateAlmacenUseCase {
    private AlmacenesRepository repository;

    public CreateAlmacenUseCase(AlmacenesRepository repository){
        this.repository = repository;
    }

    public boolean execute (String nombre) {
        long timeStamp = System.currentTimeMillis();
        var nuevoAlmacen = new Almacen();
        nuevoAlmacen.setNombre(nombre);
        nuevoAlmacen.setFechaHoraCreacion(String.valueOf(timeStamp));
        nuevoAlmacen.setFechaHoraUltimaMod(String.valueOf(timeStamp));
        nuevoAlmacen.setUltimoUsuario("ADMIN");


        return repository.createAlmacen(nuevoAlmacen);

    }
}
