package mx.unison.mocks.productos;

import mx.unison.presentation.productos.CreateProductoView;

public class CreateProductoViewMock implements CreateProductoView {
    public int llamadasError = 0;
    public String ultimoMensaje;
    public boolean fueCerrada = false;

    @Override
    public void close() {
        fueCerrada = true;
    }

    @Override
    public void showError(String message) {
        this.llamadasError++;
        this.ultimoMensaje = message;
    }

}
