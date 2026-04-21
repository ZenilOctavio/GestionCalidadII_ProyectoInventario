package mx.unison.presentation.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.presentation.navigation.Navigator;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.almacenes.DeleteAlmacenUseCase;

import java.util.List;

public class AlmacenesPresenter {
    private final Navigator navigator;
    private AlmacenesView view;
    private final GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private final DeleteAlmacenUseCase deleteAlmacenUseCase;

    public AlmacenesPresenter(Navigator navigator, GetAllAlmacenesUseCase getAllAlmacenesUseCase, DeleteAlmacenUseCase deleteAlmacenUseCase) {
        this.navigator = navigator;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.deleteAlmacenUseCase = deleteAlmacenUseCase;
    }
    public void setView(AlmacenesView view){
        this.view = view;
        refreshAlmacenList();
    }

    public void onRegresarClicked(){ navigator.navigateToHome(); }
    public void onAgregarClicked(){ navigator.openCreateAlmacenDialog((this::refreshAlmacenList)); }
    public void onModificarClicked(int id){navigator.openModifyAlmacenDialog(id, this::refreshAlmacenList);}
    public void onEliminarClicked(int id, String nombre){

        boolean confirmed = view.confirmAction("¿Estás seguro de eliminar '" + nombre + "'?");

        if (!confirmed)
            return;

        try {

            var success = deleteAlmacenUseCase.execute(id);

            if (!success) {
                view.showError("No se pudo eliminar el almacen");
                return;
            }

            refreshAlmacenList();

        } catch (Exception e) {
            view.showError("No se pudo eliminar: " + e.getMessage());
        }
    }

    public void refreshAlmacenList() {

        List<Almacen> almacenes = getAllAlmacenesUseCase.execute();

        view.refreshTable(almacenes);

    }
}
