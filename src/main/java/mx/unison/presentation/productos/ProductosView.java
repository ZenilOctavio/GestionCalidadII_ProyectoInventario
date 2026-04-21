package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Producto;

import java.util.List;

public interface ProductosView {
    void refreshTable(List<Producto> productos);
    boolean confirmAction(String message);
    void showError(String message);
}
