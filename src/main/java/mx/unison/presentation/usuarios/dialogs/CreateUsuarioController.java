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
import mx.unison.usecases.usuarios.CreateUsuarioUseCase;

import java.io.IOException;

/**
 * Controlador para el diálogo de creación de un nuevo usuario.
 * Permite definir el nombre de usuario, contraseña y asignar un rol
 * de acceso al sistema (ADMIN, PRODUCTOS, ALMACEN).
 */
public class CreateUsuarioController {
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

    private CreateUsuarioUseCase createUsuarioUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;

    /**
     * Constructor del controlador.
     *
     * @param createUsuarioUseCase Caso de uso para registrar el usuario.
     * @param onSuccess Callback de éxito.
     */
    public CreateUsuarioController(CreateUsuarioUseCase createUsuarioUseCase, Runnable onSuccess) {
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.onSuccess = onSuccess;
    }

    /**
     * Muestra el diálogo modal.
     *
     * @param owner Stage padre.
     */
    public void show(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/create_usuario_dialog.fxml"));
            loader.setController(this);
            VBox rootNode = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Nuevo Usuario");

            Scene scene = new Scene(rootNode);
            scene.setFill(Color.web(ThemeConfig.Colors.BG_PRIMARY));
            dialogStage.setScene(scene);

            setupStyles();
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
        }
    }

    /**
     * Inicialización de JavaFX.
     * Configura la lista de roles disponibles.
     */
    @FXML
    private void initialize() {
        // Cargar roles
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
        Label titleLabel = ComponentFactory.createTitleLabel("Nuevo Usuario");
        titleLabel.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_2XL));
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Crea una nueva cuenta de acceso");
        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Labels
        nombreLabel.setStyle(ThemeConfig.getLabelStyle());
        passwordLabel.setStyle(ThemeConfig.getLabelStyle());
        confirmPasswordLabel.setStyle(ThemeConfig.getLabelStyle());
        rolLabel.setStyle(ThemeConfig.getLabelStyle());

        // Fields
        nombreField.setStyle(ThemeConfig.getTextFieldStyle());
        passwordField.setStyle(ThemeConfig.getTextFieldStyle());
        confirmPasswordField.setStyle(ThemeConfig.getTextFieldStyle());
        rolCombo.setStyle(ThemeConfig.getTextFieldStyle());

        // Error Label
        errorLabel.setStyle(ThemeConfig.getErrorTextStyle());

        // Buttons
        Button styledCancel = ComponentFactory.createSecondaryButton("Cancelar");
        Button styledSave = ComponentFactory.createPrimaryButton("Guardar Usuario");
        
        // Reemplazar botones del FXML con los de la fábrica para asegurar estilo completo
        buttonContainer.getChildren().clear();
        buttonContainer.getChildren().addAll(styledCancel, styledSave);
        
        styledCancel.setOnAction(e -> handleCancel());
        styledSave.setOnAction(e -> handleSave());
    }

    /**
     * Maneja el proceso de guardado, validando contraseñas y campos obligatorios.
     */
    private void handleSave() {
        String nombre = nombreField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String rol = rolCombo.getValue();

        if (nombre.isEmpty()) {
            showError("Ingresa un nombre de usuario");
            return;
        }

        if (password.isEmpty()) {
            showError("Ingresa una contraseña");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden");
            return;
        }

        if (rol == null) {
            showError("Selecciona un rol");
            return;
        }

        try {
            boolean success = createUsuarioUseCase.execute(nombre, password, rol);

            if (success) {
                if (onSuccess != null) onSuccess.run();
                dialogStage.close();
            } else {
                showError("No se pudo crear el usuario");
            }
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
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