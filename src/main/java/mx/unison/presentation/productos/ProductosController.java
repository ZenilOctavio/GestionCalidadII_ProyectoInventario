package mx.unison.presentation.productos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import mx.unison.core.domain.models.Producto;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.productos.dialogs.CreateProductoController;
import mx.unison.presentation.productos.dialogs.ModifyProductoController;
import mx.unison.usecases.productos.GetAllProductosUseCase;
import mx.unison.usecases.productos.DeleteProductUseCase;
import mx.unison.usecases.productos.CreateProductoUseCase;
import mx.unison.usecases.productos.ModifyProductoUseCase;
import mx.unison.usecases.productos.FindByIdProductoUseCase;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;

import java.io.IOException;

public class ProductosController {
    @FXML
    private TableView<Producto> productosTable;
    @FXML
    private TableColumn<Producto, Integer> idColumn;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, Double> precioColumn;
    @FXML
    private TableColumn<Producto, Integer> cantidadColumn;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;
    @FXML
    private VBox root;

    private AppNavigatorFX navigator;
    private GetAllProductosUseCase getAllProductosUseCase;
    private DeleteProductUseCase deleteProductUseCase;
    private CreateProductoUseCase createProductoUseCase;
    private ModifyProductoUseCase modifyProductoUseCase;
    private FindByIdProductoUseCase findByIdProductoUseCase;
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;

    public ProductosController(AppNavigatorFX navigator,
                               GetAllProductosUseCase getAllProductosUseCase,
                               DeleteProductUseCase deleteProductUseCase) {
        this.navigator = navigator;
        this.getAllProductosUseCase = getAllProductosUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

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

    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/productos.fxml"));
            loader.setController(this);
            VBox root = loader.load();  // ✓ Correcto: VBox
            return new Scene(root, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar productos.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        nombreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre())
        );
        precioColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject()
        );
        cantidadColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCantidad())
        );

        loadProductos();

        createButton.setOnAction(e -> handleCreate());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        backButton.setOnAction(e -> handleBack());
    }

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

    private void handleBack() {
        navigator.navigateTo(AppNavigatorFX.HOME);
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
}