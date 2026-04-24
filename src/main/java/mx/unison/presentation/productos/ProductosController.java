package mx.unison.presentation.productos;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mx.unison.core.domain.models.Producto;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.productos.dialogs.CreateProductoController;
import mx.unison.presentation.productos.dialogs.ModifyProductoController;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.presentation.utils.FormatterUtil;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.productos.*;

import java.io.IOException;

/**
 * Controlador para la vista de gestión de productos.
 * Esta clase permite visualizar el catálogo de artículos, sus niveles de stock,
 * precios y el almacén al que pertenecen.
 *
 * Facilita operaciones de creación, edición y eliminación de productos,
 * integrándose con múltiples casos de uso del dominio.
 */
public class ProductosController {
    @FXML
    private AnchorPane root;
    @FXML
    private VBox mainContainer;
    @FXML
    private HBox headerContainer;
    @FXML
    private VBox titleContainer;
    @FXML
    private HBox actionButtonsContainer;
    @FXML
    private VBox tableContainer;
    @FXML
    private TableView<Producto> productosTable;
    @FXML
    private TableColumn<Producto, Integer> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, String> departamentoColumn;
    @FXML
    private TableColumn<Producto, String> precioColumn;
    @FXML
    private TableColumn<Producto, String> cantidadColumn;
    @FXML
    private TableColumn<Producto, String> almacenColumn;
    @FXML
    private Label placeholderLabel;
    @FXML
    private HBox footerContainer;
    @FXML
    private Button backButton;

    private Button createButton;
    private Button editButton;
    private Button deleteButton;

    private AppNavigatorFX navigator;
    private GetAllProductosUseCase getAllProductosUseCase;
    private DeleteProductUseCase deleteProductUseCase;
    private CreateProductoUseCase createProductoUseCase;
    private ModifyProductoUseCase modifyProductoUseCase;
    private FindByIdProductoUseCase findByIdProductoUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private boolean initialized = false;

    /**
     * Constructor del controlador de productos.
     *
     * @param navigator Navegador de escenas.
     * @param getAllProductosUseCase Caso de uso para listar productos.
     * @param deleteProductUseCase Caso de uso para eliminar productos.
     * @param createProductoUseCase Caso de uso para crear productos.
     * @param modifyProductoUseCase Caso de uso para modificar productos.
     * @param findByIdProductoUseCase Caso de uso para buscar un producto por ID.
     * @param getAllAlmacenesUseCase Caso de uso para obtener almacenes disponibles.
     */
    public ProductosController(AppNavigatorFX navigator,
                               GetAllProductosUseCase getAllProductosUseCase,
                               DeleteProductUseCase deleteProductUseCase,
                               CreateProductoUseCase createProductoUseCase,
                               ModifyProductoUseCase modifyProductoUseCase,
                               FindByIdProductoUseCase findByIdProductoUseCase,
                               GetAllAlmacenesUseCase getAllAlmacenesUseCase) {
        this.navigator = navigator;
        this.getAllProductosUseCase = getAllProductosUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.createProductoUseCase = createProductoUseCase;
        this.modifyProductoUseCase = modifyProductoUseCase;
        this.findByIdProductoUseCase = findByIdProductoUseCase;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
    }

    /**
     * Crea e inicializa la escena de productos.
     *
     * @return Una nueva escena configurada.
     * @throws RuntimeException Si hay error cargando el FXML.
     */
    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/productos.fxml"));
            loader.setController(this);
            AnchorPane rootNode = loader.load();
            return new Scene(rootNode, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar productos.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Inicialización de JavaFX.
     */
    @FXML
    private void initialize() {
        setupStyles();
        buildUI();
        configureTable();
    }

    /**
     * Aplica estilos de tema a la vista.
     */
    private void setupStyles() {
        root.setStyle("-fx-background-color: " + ThemeConfig.Colors.BG_PRIMARY + ";");
        mainContainer.setStyle("-fx-background-color: transparent;");
        
        productosTable.setStyle(ThemeConfig.getTableStyle());
        placeholderLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-family: '%s';",
                ThemeConfig.Colors.FG_MUTED,
                ThemeConfig.Typography.FONT_PRIMARY
        ));
    }

    /**
     * Construye dinámicamente los botones y etiquetas de la UI.
     */
    private void buildUI() {
        // Título y Subtítulo
        Label titleLabel = ComponentFactory.createTitleLabel("Gestión de Productos");
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Catálogo completo de artículos y niveles de stock");
        titleContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Botones de Acción
        createButton = ComponentFactory.createPrimaryButton("+ Nuevo Producto");
        editButton = ComponentFactory.createSecondaryButton("✎ Editar");
        deleteButton = ComponentFactory.createDangerButton("🗑 Eliminar");

        actionButtonsContainer.getChildren().addAll(createButton, editButton, deleteButton);

        // Botón Volver
        String backButtonStyle = String.format(
                "-fx-background-color: transparent; -fx-text-fill: %s; -fx-cursor: hand; -fx-font-family: '%s';",
                ThemeConfig.Colors.FG_SECONDARY,
                ThemeConfig.Typography.FONT_PRIMARY
        );
        backButton.setStyle(backButtonStyle);

        // Handlers
        createButton.setOnAction(e -> handleCreate());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        backButton.setOnAction(e -> handleBack());
        
        backButton.setOnMouseEntered(e -> backButton.setStyle(backButtonStyle + "-fx-text-fill: " + ThemeConfig.Colors.EMERALD_500 + ";"));
        backButton.setOnMouseExited(e -> backButton.setStyle(backButtonStyle));
    }

    /**
     * Configura las columnas de la tabla de productos, incluyendo formateo de moneda y números.
     */
    private void configureTable() {
        idColumn.setCellValueFactory(cellData -> 
                new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        nombreColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getNombre())
        );
        departamentoColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getDepartamento())
        );
        
        // Formateo de Precio
        precioColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(FormatterUtil.formatCurrency(cellData.getValue().getPrecio()))
        );
        
        // Formateo de Cantidad (Stock)
        cantidadColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(FormatterUtil.formatNumber(cellData.getValue().getCantidad()))
        );

        // Almacén (si existe)
        almacenColumn.setCellValueFactory(cellData -> {
            var product = cellData.getValue();
            String almacenName = (product.getAlmacen() != null) ? product.getAlmacen().getNombre() : "N/A";
            return new SimpleStringProperty(almacenName);
        });

        // Estilo de encabezados
        idColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        nombreColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        departamentoColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        precioColumn.setStyle(ThemeConfig.getTableHeaderStyle() + "-fx-alignment: CENTER-RIGHT;");
        cantidadColumn.setStyle(ThemeConfig.getTableHeaderStyle() + "-fx-alignment: CENTER-RIGHT;");
        almacenColumn.setStyle(ThemeConfig.getTableHeaderStyle());

        // Resaltado de fila seleccionada
        productosTable.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    row.setStyle("-fx-background-color: " + ThemeConfig.Colors.EMERALD_200 + "; " +
                                "-fx-border-color: " + ThemeConfig.Colors.EMERALD_400 + "; " +
                                "-fx-border-width: 0 0 2 0;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

        productosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Se invoca cuando la vista es mostrada.
     * Carga los productos si el usuario tiene permisos.
     */
    public void onSceneShown() {
        applyFadeInAnimation(root);
        
        if (initialized) return;
        initialized = true;

        var session = SessionManager.getInstance();
        if (!session.canManageProducts()) {
            showError("Acceso denegado", "No tienes permisos para acceder a esta sección");
            handleBack();
            return;
        }

        loadProductos();
    }

    /**
     * Carga los productos desde el repositorio y los asigna a la tabla.
     */
    private void loadProductos() {
        try {
            var productos = getAllProductosUseCase.execute();
            ObservableList<Producto> observableList = FXCollections.observableArrayList(productos);
            productosTable.setItems(observableList);
            System.out.println("✓ Productos cargados: " + productos.size());
        } catch (Exception e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
    }

    /**
     * Abre el diálogo para crear un nuevo producto.
     */
    private void handleCreate() {
        if (createProductoUseCase == null || getAllAlmacenesUseCase == null) {
            showWarning("No disponible", "Las operaciones de creación no están disponibles");
            return;
        }

        CreateProductoController dialog = new CreateProductoController(
                createProductoUseCase,
                getAllAlmacenesUseCase,
                this::loadProductos
        );
        dialog.show(navigator.getStage());
    }

    /**
     * Abre el diálogo para editar el producto seleccionado.
     */
    private void handleEdit() {
        Producto selected = productosTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un producto para editar");
            return;
        }

        if (modifyProductoUseCase == null || findByIdProductoUseCase == null || getAllAlmacenesUseCase == null) {
            showWarning("No disponible", "Las operaciones de edición no están disponibles");
            return;
        }

        ModifyProductoController dialog = new ModifyProductoController(
                modifyProductoUseCase,
                findByIdProductoUseCase,
                getAllAlmacenesUseCase,
                this::loadProductos
        );
        dialog.show(navigator.getStage(), selected.getId());
    }

    /**
     * Elimina el producto seleccionado tras confirmación.
     */
    private void handleDelete() {
        Producto selected = productosTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un producto para eliminar");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar el producto '" + selected.getNombre() + "'?");

        var result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = deleteProductUseCase.execute(selected.getId());
        if (success) {
            System.out.println("✓ Producto eliminado: " + selected.getNombre());
            loadProductos();
        } else {
            showError("Error", "No se pudo eliminar el producto");
        }
    }

    /**
     * Regresa a la vista principal.
     */
    private void handleBack() {
        applyFadeOutAnimation(() -> navigator.navigateTo(AppNavigatorFX.HOME));
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void applyFadeInAnimation(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void applyFadeOutAnimation(Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), root);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> onFinished.run());
        fade.play();
    }
}