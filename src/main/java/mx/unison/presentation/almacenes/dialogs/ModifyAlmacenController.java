package mx.unison.presentation.almacenes.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unison.core.domain.models.Almacen;
import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;

import java.io.IOException;

public class ModifyAlmacenController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField descripcionField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private VBox root;

    private ModifyAlmacenUseCase modifyAlmacenUseCase;
    private FindByIdAlmacenUseCase findByIdAlmacenUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;
    private Almacen almacen;

    public ModifyAlmacenController(ModifyAlmacenUseCase modifyAlmacenUseCase,
                                   FindByIdAlmacenUseCase findByIdAlmacenUseCase,
                                   Runnable onSuccess) {
        this.modifyAlmacenUseCase = modifyAlmacenUseCase;
        this.findByIdAlmacenUseCase = findByIdAlmacenUseCase;
        this.onSuccess = onSuccess;
    }

    public void show(Stage owner, int almacenId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/ModifyAlmacenDialog.fxml"));
            loader.setController(this);
            VBox root = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Modificar Almacén");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            loadAlmacen(almacenId);
            initialize();
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
        }
    }

    private void loadAlmacen(int almacenId) {
        var almacenOpt = findByIdAlmacenUseCase.execute(almacenId);
        if (almacenOpt.isEmpty()) {
            System.err.println("Almacén no encontrado");
            return;
        }

        this.almacen = almacenOpt.get();
        idField.setText(String.valueOf(almacen.getId()));
        nombreField.setText(almacen.getNombre());
    }

    @FXML
    private void initialize() {
        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        String nombre = nombreField.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            showError("Nombre vacío", "Por favor ingrese el nombre del almacén");
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

        // Modificar almacén
        boolean success = modifyAlmacenUseCase.execute(almacen.getId(), nombre);

        if (!success) {
            showError("Error", "No se pudo modificar el almacén");
            return;
        }

        System.out.println("✓ Almacén modificado: " + nombre);
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