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
import mx.unison.core.domain.models.Producto;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.FindByIdProductoUseCase;
import mx.unison.usecases.productos.ModifyProductoUseCase;

import java.io.IOException;

/**
 * Controlador para el diálogo de modificación de un producto existente.
 * Permite actualizar los detalles de un artículo del inventario, incluyendo
 * su nombre, descripción, precio, cantidad y almacén.
 */
public class ModifyProductoController {
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

    private ModifyProductoUseCase modifyProductoUseCase;
    private FindByIdProductoUseCase findByIdProductoUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private Runnable onSuccess;
    private Stage dialogStage;
    private int currentProductId;

    /**
     * Constructor del controlador de modificación.
     *
     * @param modifyProductoUseCase Caso de uso para aplicar los cambios.
     * @param findByIdProductoUseCase Caso de uso para recuperar los datos actuales.
     * @param getAllAlmacenesUseCase Caso de uso para listar almacenes.
     * @param onSuccess Callback de éxito.
     */
    public ModifyProductoController(ModifyProductoUseCase modifyProductoUseCase,
                                    FindByIdProductoUseCase findByIdProductoUseCase,
                                    GetAllAlmacenesUseCase getAllAlmacenesUseCase,
                                    Runnable onSuccess) {
        this.modifyProductoUseCase = modifyProductoUseCase;
        this.findByIdProductoUseCase = findByIdProductoUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.onSuccess = onSuccess;
    }

    /**
     * Muestra el diálogo de modificación para un producto específico.
     *
     * @param owner Stage padre.
     * @param productId ID del producto a modificar.
     */
    public void show(Stage owner, int productId) {
        this.currentProductId = productId;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_producto_dialog.fxml"));
            loader.setController(this);
            VBox rootNode = loader.load();

            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("Modificar Producto");

            Scene scene = new Scene(rootNode);
            scene.setFill(Color.web(ThemeConfig.Colors.BG_PRIMARY));
            dialogStage.setScene(scene);

            setupStyles();
            loadProductData();
            
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
     * Carga los almacenes en el ComboBox.
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
     * Carga la información actual del producto y la asigna a los campos.
     */
    private void loadProductData() {
        var op = findByIdProductoUseCase.execute(currentProductId);
        if (op.isPresent()) {
            var p = op.get();
            nombreField.setText(p.getNombre());
            descripcionField.setText(p.getDescripcion());
            precioField.setText(String.valueOf(p.getPrecio()));
            cantidadField.setText(String.valueOf(p.getCantidad()));
            
            // Seleccionar almacén actual
            if (p.getAlmacen() != null) {
                for (Almacen a : almacenCombo.getItems()) {
                    if (a.getId() == p.getAlmacen().getId()) {
                        almacenCombo.setValue(a);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Aplica los estilos del tema.
     */
    private void setupStyles() {
        root.setStyle(ThemeConfig.getContainerStyle());

        // Header
        Label titleLabel = ComponentFactory.createTitleLabel("Modificar Producto");
        titleLabel.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_2XL));
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Actualiza los detalles del artículo");
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
        Button styledSave = ComponentFactory.createPrimaryButton("Guardar Cambios");
        
        buttonContainer.getChildren().clear();
        buttonContainer.getChildren().addAll(styledCancel, styledSave);
        
        styledCancel.setOnAction(e -> dialogStage.close());
        styledSave.setOnAction(e -> handleSave());
    }

    /**
     * Maneja el proceso de guardado de los cambios.
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

            boolean success = modifyProductoUseCase.execute(
                    currentProductId, nombre, precio, cantidad, descripcion , almacen.getId()
            );

            if (success) {
                if (onSuccess != null) onSuccess.run();
                dialogStage.close();
            } else {
                showError("No se pudo modificar el producto");
            }
        } catch (NumberFormatException e) {
            showError("Precio o cantidad inválidos");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}