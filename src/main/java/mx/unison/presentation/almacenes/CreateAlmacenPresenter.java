package mx.unison.presentation.almacenes;

import mx.unison.usecases.almacenes.CreateAlmacenUseCase;

public class CreateAlmacenPresenter {
    private CreateAlmacenView view;
    private CreateAlmacenUseCase useCase;
    private final Runnable onSaveSuccess;

    public CreateAlmacenPresenter(CreateAlmacenUseCase useCase, Runnable onSaveSuccess) {
        this.useCase = useCase;
        this.onSaveSuccess = onSaveSuccess;
    }

    void setView(CreateAlmacenView view){
        this.view = view;
    }

    void onSaveAlmacen(String nombre){

        nombre = nombre.trim();

        if (nombre.isEmpty()) { view.showError("El nombre esta vacio"); return; }
        if (nombre.length() > 64){view.showError("El nombre no puede ser mayor a 64 caracteres"); return;}
        if (nombre.length() < 10) {view.showError("El nombre no puede ser menor a 10 caracteres"); return;}
        String regex = "^[a-zA-Z0-9 ]+$";

        if (!nombre.matches(regex)) {
            view.showError("El nombre solo puede contener letras y números.");
            return;
        }

        var success = useCase.execute(nombre);

        if (!success) {
            view.showError("No se pudo crear el almacen");
            return;
        }

        if (onSaveSuccess != null) {
            onSaveSuccess.run();
        }

        view.close();

    }
}
