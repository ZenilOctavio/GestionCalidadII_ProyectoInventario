package mx.unison.mocks.almacenes;

import mx.unison.presentation.almacenes.CreateAlmacenView;

public class CreateAlmacenViewMock implements CreateAlmacenView {
    public int llamadasError = 0;
    public boolean fueCerrada = false;
    public String ultimoMensaje;

    @Override
    public void close() {
        this.fueCerrada = true;
    }

    @Override
    public void showError(String message) {
        this.llamadasError++;
        this.ultimoMensaje = message;
    }

    public boolean errorMostrado() { return llamadasError > 0; }
}
