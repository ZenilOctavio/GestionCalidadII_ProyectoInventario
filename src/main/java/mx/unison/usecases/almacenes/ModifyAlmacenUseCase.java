package mx.unison.usecases.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;

public class ModifyAlmacenUseCase {
    private AlmacenesRepository repository;

    public ModifyAlmacenUseCase(AlmacenesRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int id, String nombre){
        long timeStamp = System.currentTimeMillis();
        var nuevoAlmacen = new Almacen.Builder()
                .setId(id)
                .setNombre(nombre)
                .setFechaHoraUltimaMod(String.valueOf(timeStamp))
                .setUltimoUsuario("ADMIN")
                .build();

        return repository.updateAlmacen(nuevoAlmacen);

    }
}
