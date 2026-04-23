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
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.CreateProductoUseCase;

import java.io.IOException;
import java.util.List;

public class CreateProductoController {
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

    private CreateProductoUseCase createProductoUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;

    public CreateProductoController(CreateProductoUseCase createProductoUseCase,
                                    GetAllAlmacenesUseCase getAllAlmacenesUseCase,
                                    Runnable onSuccess) {
        this.createProductoUseCase = createProductoUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.onSuccess = onSuccess;
    }

    public void show(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/create_producto_dialog.fxml"));
            loader.setController(this);
            VBox root = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Crear Producto");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            initialize();
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
        }
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

        // Crear producto
        boolean success = createProductoUseCase.execute(
                nombre, precio, cantidad, descripcion, almacenSeleccionado.getId()
        );

        if (!success) {
            showError("Error", "No se pudo crear el producto");
            return;
        }

        System.out.println("✓ Producto creado: " + nombre);
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