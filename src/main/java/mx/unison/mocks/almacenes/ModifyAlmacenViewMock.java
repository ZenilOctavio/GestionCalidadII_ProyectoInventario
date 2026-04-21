package mx.unison.mocks.almacenes;

import mx.unison.presentation.almacenes.ModifyAlmacenView;

public class ModifyAlmacenViewMock implements ModifyAlmacenView {
    public int llamadasError = 0;
    public boolean formularioInicializado = false;
    public boolean fueCerrada = false;

    @Override
    public void close() { this.fueCerrada = true; }

    @Override
    public void initializeForm(String nombre) {
        this.formularioInicializado = true;
    }

    @Override
    public void showError(String message) { this.llamadasError++; }

    public boolean errorMostrado() { return llamadasError > 0; }
}