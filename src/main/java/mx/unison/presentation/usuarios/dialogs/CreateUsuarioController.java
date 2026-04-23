package mx.unison.presentation.usuarios.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import mx.unison.usecases.usuarios.CreateUsuarioUseCase;

import java.io.IOException;

public class CreateUsuarioController {
    @FXML
    private TextField nombreField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> rolCombo;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private VBox root;

    private CreateUsuarioUseCase createUsuarioUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;

    public CreateUsuarioController(CreateUsuarioUseCase createUsuarioUseCase, Runnable onSuccess) {
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.onSuccess = onSuccess;
    }

    public void show(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/create_usuario_dialog.fxml"));
            loader.setController(this);
            VBox root = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Crear Usuario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            initialize();
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // ✓ Cargar roles disponibles
        rolCombo.setItems(FXCollections.observableArrayList(
                "ADMIN",
                "PRODUCTOS",
                "ALMACEN"
        ));

        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        String nombre = nombreField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String rol = rolCombo.getValue();

        if (nombre.isEmpty()) {
            showError("Nombre vacío", "Por favor ingrese el nombre de usuario");
            return;
        }

        if (nombre.length() < 3) {
            showError("Nombre muy corto", "El nombre debe tener al menos 3 caracteres");
            return;
        }

        if (password.isEmpty()) {
            showError("Contraseña vacía", "Por favor ingrese una contraseña");
            return;
        }

        if (password.length() < 6) {
            showError("Contraseña muy corta", "La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Contraseñas no coinciden", "Las contraseñas no son iguales");
            return;
        }

        if (rol == null) {
            showError("Rol no seleccionado", "Por favor seleccione un rol");
            return;
        }

        boolean success = createUsuarioUseCase.execute(nombre, password, rol);

        if (!success) {
            showError("Error", "No se pudo crear el usuario");
            return;
        }

        System.out.println("✓ Usuario creado: " + nombre);
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