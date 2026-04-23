package mx.unison.presentation.usuarios.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.usecases.usuarios.ModifyUsuarioUseCase;

import java.io.IOException;

public class ModifyUsuarioController {
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

    private ModifyUsuarioUseCase modifyUsuarioUseCase;
    private UsersRepository usuariosRepository;
    private Runnable onSuccess;
    private Stage dialogStage;
    private Usuario usuario;

    public ModifyUsuarioController(ModifyUsuarioUseCase modifyUsuarioUseCase,
                                   UsersRepository usuariosRepository,
                                   Runnable onSuccess) {
        this.modifyUsuarioUseCase = modifyUsuarioUseCase;
        this.usuariosRepository = usuariosRepository;
        this.onSuccess = onSuccess;
    }

    // Constructor simplificado
    public ModifyUsuarioController(ModifyUsuarioUseCase modifyUsuarioUseCase, Runnable onSuccess) {
        this.modifyUsuarioUseCase = modifyUsuarioUseCase;
        this.onSuccess = onSuccess;
    }

    public void show(Stage owner, String usuarioNombre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_usuario_dialog.fxml"));
            loader.setController(this);
            VBox root = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Modificar Usuario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            loadUsuario(usuarioNombre);
            initialize();
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUsuario(String usuarioNombre) {
        // Para cargar usuario, se pasa mediante el constructor
        nombreField.setText(usuarioNombre);
    }

    @FXML
    private void initialize() {
        rolCombo.setItems(FXCollections.observableArrayList(
                "ADMIN",
                "PRODUCTOS",
                "ALMACEN"
        ));

        if (usuario != null) {
            nombreField.setText(usuario.getNombre());
            rolCombo.setValue(usuario.getRol());
        }

        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        String nombre = nombreField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String rol = rolCombo.getValue();

        if (rol == null) {
            showError("Rol no seleccionado", "Por favor seleccione un rol");
            return;
        }

        // Si se ingresó contraseña, validar
        if (!password.isEmpty()) {
            if (password.length() < 6) {
                showError("Contraseña muy corta", "La contraseña debe tener al menos 6 caracteres");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Contraseñas no coinciden", "Las contraseñas no son iguales");
                return;
            }
        } else {
            password = null;  // No cambiar contraseña
        }

        boolean success = modifyUsuarioUseCase.execute(nombre, password, rol);

        if (!success) {
            showError("Error", "No se pudo modificar el usuario");
            return;
        }

        System.out.println("✓ Usuario modificado: " + nombre);
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