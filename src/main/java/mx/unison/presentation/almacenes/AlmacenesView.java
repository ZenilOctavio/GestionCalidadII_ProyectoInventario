package mx.unison.presentation.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;

import java.util.List;

public interface AlmacenesView {
    void refreshTable(List<Almacen> almacenes);
    boolean confirmAction(String message);
    void showError(String message);
}
