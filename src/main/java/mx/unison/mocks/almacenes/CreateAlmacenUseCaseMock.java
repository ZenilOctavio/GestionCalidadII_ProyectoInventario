package mx.unison.mocks.almacenes;

import mx.unison.usecases.almacenes.CreateAlmacenUseCase;

public class CreateAlmacenUseCaseMock extends CreateAlmacenUseCase {
    public boolean resultToReturn = true;
    public boolean executeCalled = false;
    public String nombreRecibido;

    public CreateAlmacenUseCaseMock() {
        super(null); // No necesitamos el repositorio real
    }

    @Override
    public boolean execute(String nombre) {
        this.executeCalled = true;
        this.nombreRecibido = nombre;
        return resultToReturn;
    }
}
