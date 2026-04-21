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
        var nuevoAlmacen = new Almacen.Builder()
                .setNombre(nombre)
                .setFechaHoraCreacion(String.valueOf(timeStamp))
                .setFechaHoraUltimaMod(String.valueOf(timeStamp))
                .setUltimoUsuario("ADMIN")
                .build();

        return repository.createAlmacen(nuevoAlmacen);

    }
}
