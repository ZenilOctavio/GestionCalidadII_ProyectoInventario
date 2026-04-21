package mx.unison.presentation.almacenes;

public interface ModifyAlmacenView {
    void close();
    void initializeForm(String nombre);
    void showError(String message);
}
