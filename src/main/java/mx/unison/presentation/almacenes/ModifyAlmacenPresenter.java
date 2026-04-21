package mx.unison.presentation.almacenes;

import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;

public class ModifyAlmacenPresenter {
    private int idAlmacen;
    private ModifyAlmacenView view;
    private ModifyAlmacenUseCase modifyUseCase;
    private FindByIdAlmacenUseCase findUseCase;
    private final Runnable onSaveSuccess;

    public ModifyAlmacenPresenter(
            int idAlmacen,
            ModifyAlmacenUseCase modifyUseCase,
            FindByIdAlmacenUseCase findUseCase,
            Runnable onSaveSuccess
    ) {
        this.idAlmacen = idAlmacen;
        this.modifyUseCase = modifyUseCase;
        this.findUseCase = findUseCase;
        this.onSaveSuccess = onSaveSuccess;
    }

    public void setView(ModifyAlmacenView view) {
        this.view = view;
        var productoOpt = findUseCase.execute(idAlmacen);

        if (productoOpt.isEmpty()) {
            view.showError("No se encontro el producto");
            return;
        }
        var producto = productoOpt.get();
        view.initializeForm(
                producto.nombre()
        );
    }

    void onModifyAlmacen(String nombre){
        nombre = nombre.trim();

        if (nombre.isEmpty()) { view.showError("El nombre esta vacio"); return; }
        if (nombre.length() > 64){view.showError("El nombre no puede ser mayor a 64 caracteres"); return;}
        if (nombre.length() < 10) {view.showError("El nombre no puede ser menor a 10 caracteres"); return;}
        String regex = "^[a-zA-Z0-9 ]+$";
        if (!nombre.matches(regex)) {
            view.showError("El nombre solo puede contener letras y números.");
            return;
        }

        var success = modifyUseCase.execute(idAlmacen, nombre);

        if (!success) {
            view.showError("No se pudo realizar la modificacion.");
            return;
        }

        if (onSaveSuccess != null) {
            onSaveSuccess.run();
        }
        view.close();

    }


}
