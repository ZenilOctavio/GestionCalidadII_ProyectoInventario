package mx.unison.mocks.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;

import java.util.Optional;

public class FindByIdAlmacenMock extends FindByIdAlmacenUseCase {
    public Almacen almacenARetornar;
    public FindByIdAlmacenMock() { super(null); }
    @Override
    public Optional<Almacen> execute(int id) {
        return Optional.ofNullable(almacenARetornar);
    }
}
