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
import mx.unison.core.domain.models.Almacen;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;

import java.io.IOException;

/**
 * Controlador para el diálogo de modificación de un almacén existente.
 * Esta clase gestiona la edición de los datos de un almacén seleccionado,
 * cargando su información actual y permitiendo actualizarla.
 */
public class ModifyAlmacenController {
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

    private ModifyAlmacenUseCase modifyAlmacenUseCase;
    private FindByIdAlmacenUseCase findByIdAlmacenUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;
    private int currentAlmacenId;

    /**
     * Constructor del controlador de modificación.
     *
     * @param modifyAlmacenUseCase Caso de uso para actualizar el almacén.
     * @param findByIdAlmacenUseCase Caso de uso para obtener los datos actuales del almacén.
     * @param onSuccess Callback de éxito para refrescar la vista principal.
     */
    public ModifyAlmacenController(ModifyAlmacenUseCase modifyAlmacenUseCase,
                                   FindByIdAlmacenUseCase findByIdAlmacenUseCase,
                                   Runnable onSuccess) {
        this.modifyAlmacenUseCase = modifyAlmacenUseCase;
        this.findByIdAlmacenUseCase = findByIdAlmacenUseCase;
        this.onSuccess = onSuccess;
    }

    /**
     * Muestra el diálogo de modificación para un almacén específico.
     *
     * @param owner El Stage padre.
     * @param almacenId El identificador único del almacén a modificar.
     */
    public void show(Stage owner, int almacenId) {
        this.currentAlmacenId = almacenId;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_almacen_dialog.fxml"));
            loader.setController(this);
            VBox rootNode = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Modificar Almacén");

            Scene scene = new Scene(rootNode);
            scene.setFill(Color.web(ThemeConfig.Colors.BG_PRIMARY));
            dialogStage.setScene(scene);

            setupStyles();
            loadAlmacenData();
            
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al cargar diálogo: " + e.getMessage());
        }
    }

    /**
     * Configura los estilos visuales del diálogo.
     */
    private void setupStyles() {
        root.setStyle(ThemeConfig.getContainerStyle());

        // Header
        Label titleLabel = ComponentFactory.createTitleLabel("Modificar Almacén");
        titleLabel.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_2XL));
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Actualiza el nombre del depósito");
        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Labels
        nombreLabel.setStyle(ThemeConfig.getLabelStyle());

        // Fields
        nombreField.setStyle(ThemeConfig.getTextFieldStyle());

        // Error Label
        errorLabel.setStyle(ThemeConfig.getErrorTextStyle());

        // Buttons
        Button styledCancel = ComponentFactory.createSecondaryButton("Cancelar");
        Button styledSave = ComponentFactory.createPrimaryButton("Guardar Cambios");
        
        buttonContainer.getChildren().clear();
        buttonContainer.getChildren().addAll(styledCancel, styledSave);
        
        styledCancel.setOnAction(e -> dialogStage.close());
        styledSave.setOnAction(e -> handleSave());
    }

    /**
     * Carga los datos actuales del almacén en los campos del formulario.
     */
    private void loadAlmacenData() {
        var pa = findByIdAlmacenUseCase.execute(currentAlmacenId);
        pa.ifPresent(a -> nombreField.setText(a.getNombre()));
    }

    /**
     * Maneja la lógica de guardado de los cambios realizados.
     */
    private void handleSave() {
        String nombre = nombreField.getText().trim();

        if (nombre.isEmpty()) {
            showError("Ingresa el nombre del almacén");
            return;
        }

        try {
            boolean success = modifyAlmacenUseCase.execute(currentAlmacenId, nombre);

            if (success) {
                if (onSuccess != null) onSuccess.run();
                dialogStage.close();
            } else {
                showError("No se pudo modificar el almacén");
            }
        }
        catch(IllegalArgumentException e){
            showError(e.getMessage());
        }
    }

    /**
     * Muestra un mensaje de error.
     * @param message Mensaje a mostrar.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}