package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Almacen;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.CreateProductoUseCase;

import java.util.List;

public class CreateProductoPresenter {
    private CreateProductoView view;
    private CreateProductoUseCase useCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private final Runnable onSaveSuccess;

    public CreateProductoPresenter(CreateProductoUseCase useCase, GetAllAlmacenesUseCase getAllAlmacenesUseCase, Runnable onSaveSuccess) {
        this.useCase = useCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.onSaveSuccess = onSaveSuccess;
    }

    void setView(CreateProductoView view){
        this.view = view;
    }

    List<Almacen> getAlmacenes(){
        return getAllAlmacenesUseCase.execute();
    }

    void onSaveProducto(String nombre, String precioStr, String cantidadStr, String descripcion, int idAlmacen){

        double precio;
        int cantidad;

        nombre = nombre.trim();
        precioStr = precioStr.trim();
        cantidadStr = cantidadStr.trim();

        if (nombre.isEmpty()) { view.showError("El nombre esta vacio"); return; }

        if (nombre.length() > 64){view.showError("El nombre no puede ser mayor a 32 caracteres"); return;}
        if (nombre.length() < 10) {view.showError("El nombre no puede ser menor a 10 caracteres"); return;}
        String regex = "^[a-zA-Z0-9 ]+$";

        if (!nombre.matches(regex)) {
            view.showError("El nombre solo puede contener letras y números.");
            return;
        }

        if (descripcion.length() > 255) { view.showError("La descripcion no puede ser mayor a 255 caracteres"); return;}

        if (precioStr.isEmpty()) { view.showError("El precio esta vacio"); return; }
        if (cantidadStr.isEmpty()) { view.showError("La cantidad esta vacia"); return; }

        try {
            precio = Double.parseDouble(precioStr);
        } catch(NumberFormatException ex){
            view.showError("El precio no esta en formato correcto");
            return;
        }

        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch(NumberFormatException ex){
            view.showError("La cantidad no esta en formato correcto");
            return;
        }

        var success = useCase.execute(nombre, precio, cantidad, descripcion, idAlmacen);

        if (!success) {
            view.showError("No se pudo crear el producto");
            return;
        }

        if (onSaveSuccess != null) {
            onSaveSuccess.run();
        }

        view.close();

    }
}
