package mx.unison.presentation.navigation;

public interface Navigator {
    void navigateToLogin();
    void navigateToHome();
    void navigateToProductosPanel();
    void navigateToAlmacenesPanel();

    void openCreateProductDialog(Runnable onSaveSuccess);
    void openModifyProductDialog(int idProducto, Runnable onSaveSuccess);

    void openCreateAlmacenDialog(Runnable onSaveSuccess);
    void openModifyAlmacenDialog(int idAlmacen, Runnable onSaveSuccess);
}
