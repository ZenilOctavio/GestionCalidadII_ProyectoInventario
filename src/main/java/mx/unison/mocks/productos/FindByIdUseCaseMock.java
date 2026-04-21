package mx.unison.mocks.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.usecases.productos.FindByIdProductoUseCase;

import java.util.Optional;

public class FindByIdUseCaseMock extends FindByIdProductoUseCase {
    public Producto productoRetornado;
    public FindByIdUseCaseMock() { super(null); }
    @Override
    public Optional<Producto> execute(int id) {
        return Optional.ofNullable(productoRetornado);
    }
}
