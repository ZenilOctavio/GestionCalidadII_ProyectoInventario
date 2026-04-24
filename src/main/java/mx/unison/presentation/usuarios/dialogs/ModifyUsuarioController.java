package mx.unison.presentation.usuarios.dialogs;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.usecases.usuarios.ModifyUsuarioUseCase;

import java.io.IOException;

/**
 * Controlador para el diálogo de modificación de un usuario existente.
 * Permite actualizar la contraseña y el rol de un usuario.
 * Por seguridad, el nombre de usuario no es editable.
 */
public class ModifyUsuarioController {
    @FXML
    private VBox root;
    @FXML
    private VBox headerContainer;
    @FXML
    private VBox formFields;
    @FXML
    private TextField nombreField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> rolCombo;
    @FXML
    private Label nombreLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Label rolLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private HBox buttonContainer;

    private ModifyUsuarioUseCase modifyUsuarioUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;
    private String currentUsername;

    /**
     * Constructor del controlador.
     *
     * @param modifyUsuarioUseCase Caso de uso para aplicar las modificaciones.
     * @param onSuccess Callback de éxito.
     */
    public ModifyUsuarioController(ModifyUsuarioUseCase modifyUsuarioUseCase, Runnable onSuccess) {
        this.modifyUsuarioUseCase = modifyUsuarioUseCase;
        this.onSuccess = onSuccess;
    }

    /**
     * Muestra el diálogo modal para modificar un usuario.
     *
     * @param owner Stage padre.
     * @param username Nombre del usuario a modificar.
     */
    public void show(Stage owner, String username) {
        this.currentUsername = username;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_usuario_dialog.fxml"));
            loader.setController(this);
            VBox rootNode = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Modificar Usuario");

            Scene scene = new Scene(rootNode);
            scene.setFill(Color.web(ThemeConfig.Colors.BG_PRIMARY));
            dialogStage.setScene(scene);

            setupStyles();
            
            // Cargar datos actuales
            nombreField.setText(username);
            
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
        }
    }

    /**
     * Inicialización de JavaFX.
     */
    @FXML
    private void initialize() {
        rolCombo.setItems(FXCollections.observableArrayList(
                "ADMIN",
                "PRODUCTOS",
                "ALMACEN"
        ));

        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());
    }

    /**
     * Aplica los estilos del tema global.
     */
    private void setupStyles() {
        root.setStyle(ThemeConfig.getContainerStyle());

        // Header
        Label titleLabel = ComponentFactory.createTitleLabel("Modificar Usuario");
        titleLabel.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_2XL));
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Actualiza los permisos del usuario");
        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Labels
        nombreLabel.setStyle(ThemeConfig.getLabelStyle());
        passwordLabel.setStyle(ThemeConfig.getLabelStyle());
        confirmPasswordLabel.setStyle(ThemeConfig.getLabelStyle());
        rolLabel.setStyle(ThemeConfig.getLabelStyle());

        // Fields
        nombreField.setStyle(ThemeConfig.getTextFieldStyle() + "-fx-opacity: 0.7;");
        passwordField.setStyle(ThemeConfig.getTextFieldStyle());
        confirmPasswordField.setStyle(ThemeConfig.getTextFieldStyle());
        rolCombo.setStyle(ThemeConfig.getTextFieldStyle());

        // Error Label
        errorLabel.setStyle(ThemeConfig.getErrorTextStyle());

        // Buttons
        Button styledCancel = ComponentFactory.createSecondaryButton("Cancelar");
        Button styledSave = ComponentFactory.createPrimaryButton("Guardar Cambios");
        
        buttonContainer.getChildren().clear();
        buttonContainer.getChildren().addAll(styledCancel, styledSave);
        
        styledCancel.setOnAction(e -> handleCancel());
        styledSave.setOnAction(e -> handleSave());
    }

    /**
     * Maneja el guardado de cambios. Si la contraseña se deja vacía, no se actualiza.
     */
    private void handleSave() {
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String rol = rolCombo.getValue();

        if (!password.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                showError("Las contraseñas no coinciden");
                return;
            }
        }

        if (rol == null) {
            showError("Selecciona un rol");
            return;
        }

        boolean success = modifyUsuarioUseCase.execute(currentUsername, password, rol);

        if (success) {
            if (onSuccess != null) onSuccess.run();
            dialogStage.close();
        } else {
            showError("No se pudo modificar el usuario");
        }
    }

    private void handleCancel() {
        dialogStage.close();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}