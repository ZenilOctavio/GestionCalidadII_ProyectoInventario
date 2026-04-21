package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.FindByIdProductoUseCase;
import mx.unison.usecases.productos.ModifyProductoUseCase;

import java.util.List;

public class ModifyProductoPresenter {
    private int idProducto;
    private ModifyProductoView view;
    private ModifyProductoUseCase modifyUseCase;
    private FindByIdProductoUseCase findUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private final Runnable onSaveSuccess;

    public ModifyProductoPresenter(
            int idProducto,
            ModifyProductoUseCase modifyUseCase,
            FindByIdProductoUseCase findUseCase,
            GetAllAlmacenesUseCase getAllAlmacenesUseCase,
            Runnable onSaveSuccess
    ) {
        this.idProducto = idProducto;
        this.modifyUseCase = modifyUseCase;
        this.findUseCase = findUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.onSaveSuccess = onSaveSuccess;
    }

    public void setView(ModifyProductoView view) {
        this.view = view;
        var productoOpt = findUseCase.execute(idProducto);

        if (productoOpt.isEmpty()) {
            view.showError("No se encontro el producto");
            return;
        }
        var producto = productoOpt.get();
        view.initializeForm(
                producto.nombre(),
                producto.precio(),
                producto.cantidad(),
                producto.descripcion()
        );
    }

    void onModifyProducto(String nombre, String precioStr, String cantidadStr, String descripcion, int idAlmacen){
        double precio;
        int cantidad;

        nombre = nombre.trim();
        precioStr = precioStr.trim();
        cantidadStr = cantidadStr.trim();

        if (nombre.isEmpty()) { view.showError("El nombre esta vacio"); return; }
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

        var success = modifyUseCase.execute(idProducto, nombre, precio, cantidad, descripcion, idAlmacen);

        if (!success) {
            view.showError("No se pudo modificar el producto");
            return;
        }

        if (onSaveSuccess != null) {
            onSaveSuccess.run();
        }
        view.close();

    }
    List<Almacen> getAlmacenes(){
        return getAllAlmacenesUseCase.execute();
    }

}
