package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.presentation.navigation.Navigator;
import mx.unison.usecases.productos.DeleteProductUseCase;
import mx.unison.usecases.productos.GetAllProductosUseCase;

import java.util.List;

public class ProductosPresenter {
    private final Navigator navigator;
    private ProductosView view;
    private final GetAllProductosUseCase getAllProductosUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductosPresenter(Navigator navigator, GetAllProductosUseCase getAllProductosUseCase, DeleteProductUseCase deleteProductUseCase) {
        this.navigator = navigator;
        this.getAllProductosUseCase = getAllProductosUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }
    public void setView(ProductosView view){
        this.view = view;
        refreshProductList();
    }

    public void onRegresarClicked(){ navigator.navigateToHome(); }
    public void onAgregarClicked(){ navigator.openCreateProductDialog((this::refreshProductList)); }
    public void onModificarClicked(int id){navigator.openModifyProductDialog(id, this::refreshProductList);}
    public void onEliminarClicked(int id, String nombre){

        boolean confirmed = view.confirmAction("¿Estás seguro de eliminar '" + nombre + "'?");

        if (!confirmed)
            return;

        try {

            var success = deleteProductUseCase.execute(id);

            if (!success) {
                view.showError("No se pudo eliminar el producto");
                return;
            }

            refreshProductList();

        } catch (Exception e) {
            view.showError("No se pudo eliminar: " + e.getMessage());
        }
    }

    public void refreshProductList() {

        List<Producto> productos = getAllProductosUseCase.execute();

        view.refreshTable(productos);

    }
}
