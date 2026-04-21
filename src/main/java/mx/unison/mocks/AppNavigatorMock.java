package mx.unison.mocks;

import mx.unison.presentation.navigation.Navigator;

public class AppNavigatorMock implements Navigator {
    public boolean navegoHome=false;
    @Override
    public void navigateToLogin() {

    }

    @Override
    public void navigateToHome() {
        navegoHome = true;
    }

    @Override
    public void navigateToProductosPanel() {

    }

    @Override
    public void navigateToAlmacenesPanel() {

    }

    @Override
    public void openCreateProductDialog(Runnable onSaveSuccess) {

    }

    @Override
    public void openModifyProductDialog(int idProducto, Runnable onSaveSuccess) {

    }

    @Override
    public void openCreateAlmacenDialog(Runnable onSaveSuccess) {

    }

    @Override
    public void openModifyAlmacenDialog(int idAlmacen, Runnable onSaveSuccess) {

    }
}