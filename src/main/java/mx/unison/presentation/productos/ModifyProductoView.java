package mx.unison.presentation.productos;

public interface ModifyProductoView {
    void close();
    void initializeForm(String nombre, double precio, int cantidad, String descripcion);
    void showError(String message);
}
