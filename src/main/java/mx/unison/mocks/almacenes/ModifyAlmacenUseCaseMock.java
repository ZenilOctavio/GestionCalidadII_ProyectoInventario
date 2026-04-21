package mx.unison.mocks.almacenes;

import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;

public class ModifyAlmacenUseCaseMock extends ModifyAlmacenUseCase {
    public boolean resultToReturn = true;
    public boolean executeCalled = false;
    public ModifyAlmacenUseCaseMock() { super(null); }
    @Override
    public boolean execute(int id, String nombre) {
        this.executeCalled = true;
        return resultToReturn;
    }
}
