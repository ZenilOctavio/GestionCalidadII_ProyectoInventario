package mx.unison.presentation.productos.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.FindByIdProductoUseCase;
import mx.unison.usecases.productos.ModifyProductoUseCase;

import java.io.IOException;
import java.util.List;

public class ModifyProductoController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField descripcionField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField cantidadField;
    @FXML
    private ComboBox<Almacen> almacenCombo;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private VBox root;

    private ModifyProductoUseCase modifyProductoUseCase;
    private FindByIdProductoUseCase findByIdProductoUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;
    private Producto producto;

    public ModifyProductoController(ModifyProductoUseCase modifyProductoUseCase,
                                    FindByIdProductoUseCase findByIdProductoUseCase,
                                    GetAllAlmacenesUseCase getAllAlmacenesUseCase,
                                    Runnable onSuccess) {
        this.modifyProductoUseCase = modifyProductoUseCase;
        this.findByIdProductoUseCase = findByIdProductoUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.onSuccess = onSuccess;
    }

    public void show(Stage owner, int productoId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_producto_dialog.fxml"));
            loader.setController(this);
            VBox root = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Modificar Producto");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            loadProducto(productoId);
            initialize();
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
        }
    }

    private void loadProducto(int productoId) {
        var productoOpt = findByIdProductoUseCase.execute(productoId);
        if (productoOpt.isEmpty()) {
            System.err.println("Producto no encontrado");
            return;
        }

        this.producto = productoOpt.get();
        idField.setText(String.valueOf(producto.getId()));
        nombreField.setText(producto.getNombre());
        descripcionField.setText(producto.getDescripcion());
        precioField.setText(String.valueOf(producto.getPrecio()));
        cantidadField.setText(String.valueOf(producto.getCantidad()));
    }

    @FXML
    private void initialize() {
        loadAlmacenes();
        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());
    }

    private void loadAlmacenes() {
        try {
            List<Almacen> almacenes = getAllAlmacenesUseCase.execute();
            ObservableList<Almacen> observableAlmacenes = FXCollections.observableArrayList(almacenes);
            almacenCombo.setItems(observableAlmacenes);

            // Mostrar nombre en el ComboBox
            almacenCombo.setCellFactory(param -> new ListCell<Almacen>() {
                @Override
                protected void updateItem(Almacen item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre());
                }
            });
            almacenCombo.setButtonCell(new ListCell<Almacen>() {
                @Override
                protected void updateItem(Almacen item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre());
                }
            });

            // Seleccionar el almacén actual
            if (producto != null && producto.getAlmacen() != null) {
                for (Almacen a : almacenes) {
                    if (a.getId() == producto.getAlmacen().getId()) {
                        almacenCombo.setValue(a);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar almacenes: " + e.getMessage());
        }
    }

    private void handleSave() {
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String precioStr = precioField.getText().trim();
        String cantidadStr = cantidadField.getText().trim();
        Almacen almacenSeleccionado = almacenCombo.getValue();

        // Validaciones
        if (nombre.isEmpty()) {
            showError("Nombre vacío", "Por favor ingrese el nombre del producto");
            return;
        }

        if (nombre.length() < 10) {
            showError("Nombre muy corto", "El nombre debe tener al menos 10 caracteres");
            return;
        }

        if (nombre.length() > 64) {
            showError("Nombre muy largo", "El nombre no puede exceder 64 caracteres");
            return;
        }

        if (!nombre.matches("^[a-zA-Z0-9 ]+$")) {
            showError("Nombre inválido", "El nombre solo puede contener letras y números");
            return;
        }

        if (descripcion.length() > 255) {
            showError("Descripción muy larga", "La descripción no puede exceder 255 caracteres");
            return;
        }

        if (precioStr.isEmpty()) {
            showError("Precio vacío", "Por favor ingrese el precio");
            return;
        }

        if (cantidadStr.isEmpty()) {
            showError("Cantidad vacía", "Por favor ingrese la cantidad");
            return;
        }

        if (almacenSeleccionado == null) {
            showError("Almacén no seleccionado", "Por favor seleccione un almacén");
            return;
        }

        double precio;
        int cantidad;

        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                showError("Precio inválido", "El precio debe ser mayor a 0");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Precio inválido", "Por favor ingrese un precio válido");
            return;
        }

        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad < 0) {
                showError("Cantidad inválida", "La cantidad no puede ser negativa");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Cantidad inválida", "Por favor ingrese una cantidad válida");
            return;
        }

        // Modificar producto
        boolean success = modifyProductoUseCase.execute(
                producto.getId(),
                nombre,
                precio,
                cantidad,
                descripcion,
                almacenSeleccionado.getId()
        );

        if (!success) {
            showError("Error", "No se pudo modificar el producto");
            return;
        }

        System.out.println("✓ Producto modificado: " + nombre);
        if (onSuccess != null) {
            onSuccess.run();
        }
        dialogStage.close();
    }

    private void handleCancel() {
        dialogStage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}