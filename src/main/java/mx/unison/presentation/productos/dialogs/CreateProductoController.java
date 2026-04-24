package mx.unison.presentation.productos.dialogs;

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
import javafx.util.StringConverter;
import mx.unison.core.domain.models.Almacen;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.CreateProductoUseCase;

import java.io.IOException;

/**
 * Controlador para el diálogo de creación de un nuevo producto.
 * Gestiona la entrada de datos como nombre, descripción, precio, cantidad
 * y la asignación a un almacén específico.
 */
public class CreateProductoController {
    @FXML
    private VBox root;
    @FXML
    private VBox headerContainer;
    @FXML
    private VBox formFields;
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
    private Label nombreLabel;
    @FXML
    private Label descripcionLabel;
    @FXML
    private Label precioLabel;
    @FXML
    private Label cantidadLabel;
    @FXML
    private Label almacenLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private HBox buttonContainer;

    private CreateProductoUseCase createProductoUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;

    /**
     * Constructor del controlador.
     *
     * @param createProductoUseCase Caso de uso para crear el producto.
     * @param getAllAlmacenesUseCase Caso de uso para listar almacenes en el combo.
     * @param onSuccess Callback de éxito.
     */
    public CreateProductoController(CreateProductoUseCase createProductoUseCase,
                                    GetAllAlmacenesUseCase getAllAlmacenesUseCase,
                                    Runnable onSuccess) {
        this.createProductoUseCase = createProductoUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.onSuccess = onSuccess;
    }

    /**
     * Muestra el diálogo modal de creación de producto.
     *
     * @param owner Stage padre.
     */
    public void show(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/create_producto_dialog.fxml"));
            loader.setController(this);
            VBox rootNode = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Nuevo Producto");

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
     */
    @FXML
    private void initialize() {
        loadAlmacenes();
    }

    /**
     * Carga la lista de almacenes disponibles en el ComboBox.
     */
    private void loadAlmacenes() {
        var almacenes = getAllAlmacenesUseCase.execute();
        almacenCombo.setItems(FXCollections.observableArrayList(almacenes));
        almacenCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Almacen a) {
                return (a == null) ? "" : a.getNombre();
            }

            @Override
            public Almacen fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Aplica estilos de tema a los componentes del diálogo.
     */
    private void setupStyles() {
        root.setStyle(ThemeConfig.getContainerStyle());

        // Header
        Label titleLabel = ComponentFactory.createTitleLabel("Nuevo Producto");
        titleLabel.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_2XL));
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Registra un nuevo artículo en el inventario");
        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Labels
        Label[] labels = {nombreLabel, descripcionLabel, precioLabel, cantidadLabel, almacenLabel};
        for (Label l : labels) l.setStyle(ThemeConfig.getLabelStyle());

        // Fields
        nombreField.setStyle(ThemeConfig.getTextFieldStyle());
        descripcionField.setStyle(ThemeConfig.getTextFieldStyle());
        precioField.setStyle(ThemeConfig.getTextFieldStyle());
        cantidadField.setStyle(ThemeConfig.getTextFieldStyle());
        almacenCombo.setStyle(ThemeConfig.getTextFieldStyle());

        // Error Label
        errorLabel.setStyle(ThemeConfig.getErrorTextStyle());

        // Buttons
        Button styledCancel = ComponentFactory.createSecondaryButton("Cancelar");
        Button styledSave = ComponentFactory.createPrimaryButton("Guardar Producto");
        
        buttonContainer.getChildren().clear();
        buttonContainer.getChildren().addAll(styledCancel, styledSave);
        
        styledCancel.setOnAction(e -> dialogStage.close());
        styledSave.setOnAction(e -> handleSave());
    }

    /**
     * Maneja el guardado del producto, validando tipos de datos y campos obligatorios.
     */
    private void handleSave() {
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String precioStr = precioField.getText().trim();
        String cantidadStr = cantidadField.getText().trim();
        Almacen almacen = almacenCombo.getValue();

        if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty() || almacen == null) {
            showError("Por favor completa los campos obligatorios");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);

            boolean success = createProductoUseCase.execute(
                    nombre, precio, cantidad, descripcion, almacen.getId()
            );

            if (success) {
                if (onSuccess != null) onSuccess.run();
                dialogStage.close();
            } else {
                showError("No se pudo crear el producto");
            }
        } catch (NumberFormatException e) {
            showError("Precio o cantidad inválidos");
        }
    }

    /**
     * Muestra un error en el diálogo.
     * @param message Mensaje de error.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}