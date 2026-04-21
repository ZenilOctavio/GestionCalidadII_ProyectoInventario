package mx.unison.presentation.navigation;

import mx.unison.presentation.Vistas;
import mx.unison.presentation.factories.ViewFactory;

public class AppNavigator implements Navigator{
    private final Vistas mainFrame;
    private final ViewFactory factory;

    public static final String LOGIN = "LOGIN";
    public static final String HOME = "INICIO";
    public static final String PRODUCTOS = "PRODUCTOS";
    public static final String ALMACENES = "ALMACENES";

    public AppNavigator(Vistas mainFrame, ViewFactory factory) {
        this.mainFrame = mainFrame; this.factory = factory;
    }

    @Override
    public void navigateToLogin() {
        mainFrame.showView(LOGIN);
    }

    @Override
    public void navigateToHome() {
        mainFrame.showView(HOME);
    }

    @Override
    public void navigateToProductosPanel() {
        mainFrame.showView(PRODUCTOS);
    }

    @Override
    public void navigateToAlmacenesPanel() {
        mainFrame.showView(ALMACENES);
    }

    @Override
    public void openCreateProductDialog(Runnable onSaveSuccess) {
        var dialog = factory.makeCreateProductDialog(mainFrame, onSaveSuccess);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    @Override
    public void openModifyProductDialog(int idProducto, Runnable onSaveSuccess) {
        var dialog = factory.makeModifyProductDialog(mainFrame, idProducto, onSaveSuccess);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    @Override
    public void openCreateAlmacenDialog(Runnable onSaveSuccess) {
        var dialog = factory.makeCreateAlmacenDialog(mainFrame, onSaveSuccess);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    @Override
    public void openModifyAlmacenDialog(int idAlmacen, Runnable onSaveSuccess) {
        var dialog = factory.makeModifyAlmacenDialog(mainFrame, idAlmacen, onSaveSuccess);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }


}
