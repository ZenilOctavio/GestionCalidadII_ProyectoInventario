package mx.unison.mocks.productos;

import mx.unison.usecases.productos.ModifyProductoUseCase;

public class ModifyUseCaseMock extends ModifyProductoUseCase {
    public boolean resultToReturn = true;
    public boolean executeCalled = false;
    public ModifyUseCaseMock() { super(null); }

    @Override
    public boolean execute(int id, String n, double p, int c, String d, int idA) {
        this.executeCalled = true;
        return resultToReturn;
    }
}
