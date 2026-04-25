package mx.unison.presentation.almacenes.dialogs;

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
import mx.unison.usecases.almacenes.CreateAlmacenUseCase;

import java.io.IOException;

/**
 * Controlador para el diálogo de creación de un nuevo almacén.
 * Esta clase gestiona la ventana modal donde el usuario introduce los datos
 * para registrar un nuevo punto de distribución en el sistema.
 *
 * Sigue el patrón de diseño de controladores de JavaFX para diálogos secundarios.
 */
public class CreateAlmacenController {
    @FXML
    private VBox root;
    @FXML
    private VBox headerContainer;
    @FXML
    private TextField nombreField;
    @FXML
    private Label nombreLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private HBox buttonContainer;

    private CreateAlmacenUseCase createAlmacenUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;

    /**
     * Constructor del controlador del diálogo.
     *
     * @param createAlmacenUseCase Caso de uso para realizar la creación del almacén.
     * @param onSuccess Callback que se ejecuta tras una creación exitosa (ej. para refrescar una tabla).
     */
    public CreateAlmacenController(CreateAlmacenUseCase createAlmacenUseCase, Runnable onSuccess) {
        this.createAlmacenUseCase = createAlmacenUseCase;
        this.onSuccess = onSuccess;
    }

    /**
     * Muestra el diálogo modal.
     * Carga el FXML, configura la ventana (Stage) y aplica los estilos.
     *
     * @param owner El Stage padre sobre el cual se mostrará este diálogo modal.
     */
    public void show(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/create_almacen_dialog.fxml"));
            loader.setController(this);
            VBox rootNode = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Nuevo Almacén");

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
     * Configura los estilos visuales de los componentes del diálogo utilizando el tema global.
     */
    private void setupStyles() {
        root.setStyle(ThemeConfig.getContainerStyle());

        // Header
        Label titleLabel = ComponentFactory.createTitleLabel("Nuevo Almacén");
        titleLabel.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_2XL));
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Registra un nuevo punto de distribución");
        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Labels
        nombreLabel.setStyle(ThemeConfig.getLabelStyle());

        // Fields
        nombreField.setStyle(ThemeConfig.getTextFieldStyle());

        // Error Label
        errorLabel.setStyle(ThemeConfig.getErrorTextStyle());

        // Buttons
        Button styledCancel = ComponentFactory.createSecondaryButton("Cancelar");
        Button styledSave = ComponentFactory.createPrimaryButton("Guardar Almacén");
        
        buttonContainer.getChildren().clear();
        buttonContainer.getChildren().addAll(styledCancel, styledSave);
        
        styledCancel.setOnAction(e -> dialogStage.close());
        styledSave.setOnAction(e -> handleSave());
    }

    /**
     * Maneja la lógica de guardado del nuevo almacén.
     * Valida que el nombre no esté vacío y ejecuta el caso de uso.
     */
    private void handleSave() {
        String nombre = nombreField.getText().trim();

        if (nombre.isEmpty()) {
            showError("Ingresa el nombre del almacén");
            return;
        }

       try {
           boolean success = createAlmacenUseCase.execute(nombre);

           if (success) {
               if (onSuccess != null) onSuccess.run();
               dialogStage.close();
           } else {
               showError("No se pudo crear el almacén");
           }
       } catch(IllegalArgumentException e) {
           showError(e.getMessage());
       }
    }

    /**
     * Muestra un mensaje de error en la etiqueta correspondiente.
     *
     * @param message El mensaje de error a mostrar.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}