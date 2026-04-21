package mx.unison.usecases.almacenes;

import mx.unison.core.domain.repository.AlmacenesRepository;

public class DeleteAlmacenUseCase {
    private final AlmacenesRepository repository;

    public DeleteAlmacenUseCase(AlmacenesRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int id){
        var almacenOpt = repository.findById(id);

        if (almacenOpt.isEmpty()) return false;

        var almacen = almacenOpt.get();

        return repository.deleteAlmacen(almacen);
    }
}

