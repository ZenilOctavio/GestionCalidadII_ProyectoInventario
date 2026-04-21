package mx.unison.mocks.productos;

import mx.unison.presentation.productos.ModifyProductoView;

public class ModifyProductoViewMock implements ModifyProductoView {
    public int llamadasError = 0;
    public boolean formularioInicializado = false;
    public boolean fueCerrada = false;

    @Override
    public void close() { this.fueCerrada = true; }

    @Override
    public void initializeForm(String nombre, double precio, int cantidad, String descripcion) {
        this.formularioInicializado = true;
    }

    @Override
    public void showError(String message) { this.llamadasError++; }

    public boolean errorMostrado() { return llamadasError > 0; }
}
