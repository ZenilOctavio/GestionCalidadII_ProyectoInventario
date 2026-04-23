package mx.unison.presentation.almacenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import mx.unison.core.domain.models.Almacen;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.almacenes.dialogs.CreateAlmacenController;
import mx.unison.presentation.almacenes.dialogs.ModifyAlmacenController;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import mx.unison.usecases.almacenes.DeleteAlmacenUseCase;
import mx.unison.usecases.almacenes.CreateAlmacenUseCase;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;
import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;

import java.io.IOException;

public class AlmacenesController {
    @FXML
    private TableView<Almacen> almacenesTable;
    @FXML
    private TableColumn<Almacen, Integer> idColumn;
    @FXML
    private TableColumn<Almacen, String> nombreColumn;
    @FXML
    private TableColumn<Almacen, String> fechaCreacionColumn;
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
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private DeleteAlmacenUseCase deleteAlmacenUseCase;
    private CreateAlmacenUseCase createAlmacenUseCase;
    private ModifyAlmacenUseCase modifyAlmacenUseCase;
    private FindByIdAlmacenUseCase findByIdAlmacenUseCase;
    private boolean initialized = false;  // ✓ Flag de inicialización

    public AlmacenesController(AppNavigatorFX navigator,
                               GetAllAlmacenesUseCase getAllAlmacenesUseCase,
                               DeleteAlmacenUseCase deleteAlmacenUseCase) {
        this.navigator = navigator;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.deleteAlmacenUseCase = deleteAlmacenUseCase;
    }

    public AlmacenesController(AppNavigatorFX navigator,
                               GetAllAlmacenesUseCase getAllAlmacenesUseCase,
                               DeleteAlmacenUseCase deleteAlmacenUseCase,
                               CreateAlmacenUseCase createAlmacenUseCase,
                               ModifyAlmacenUseCase modifyAlmacenUseCase,
                               FindByIdAlmacenUseCase findByIdAlmacenUseCase) {
        this.navigator = navigator;
        this.getAllAlmacenesUseCase = getAllAlmacenesUseCase;
        this.deleteAlmacenUseCase = deleteAlmacenUseCase;
        this.createAlmacenUseCase = createAlmacenUseCase;
        this.modifyAlmacenUseCase = modifyAlmacenUseCase;
        this.findByIdAlmacenUseCase = findByIdAlmacenUseCase;
    }

    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/almacenes.fxml"));
            loader.setController(this);
            VBox root = loader.load();
            return new Scene(root, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar almacenes.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        // ✓ NO verificar permisos aquí, solo configurar componentes
        idColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        nombreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre())
        );
        fechaCreacionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaHoraCreacion())
        );

        createButton.setOnAction(e -> handleCreate());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        backButton.setOnAction(e -> handleBack());
    }

    // ✓ NUEVO: Método para inicializar permisos cuando se muestra la pantalla
    public void onSceneShown() {
        if (initialized) return;
        initialized = true;

        var session = SessionManager.getInstance();
        if (!session.canManageWarehouses()) {
            showError("Acceso denegado", "No tienes permisos para acceder a esta sección");
            handleBack();
            return;
        }

        loadAlmacenes();

        // ✓ Deshabilitar botones de creación/edición si no es ADMIN o ALMACEN
        boolean canCreate = session.canManageWarehouses();
        createButton.setDisable(!canCreate);
        editButton.setDisable(!canCreate);
        deleteButton.setDisable(!canCreate);
    }

    private void loadAlmacenes() {
        try {
            var almacenes = getAllAlmacenesUseCase.execute();
            ObservableList<Almacen> observableList = FXCollections.observableArrayList(almacenes);
            almacenesTable.setItems(observableList);
            System.out.println("✓ Almacenes cargados: " + almacenes.size());
        } catch (Exception e) {
            System.err.println("Error al cargar almacenes: " + e.getMessage());
        }
    }

    private void handleCreate() {
        if (createAlmacenUseCase == null) {
            showWarning("No disponible", "Las operaciones de creación no están disponibles");
            return;
        }

        var session = SessionManager.getInstance();
        if (!session.canManageWarehouses()) {
            showError("Acceso denegado", "No tienes permisos para crear almacenes");
            return;
        }

        CreateAlmacenController dialog = new CreateAlmacenController(
                createAlmacenUseCase,
                this::loadAlmacenes
        );
        dialog.show(navigator.getStage());
    }

    private void handleEdit() {
        Almacen selected = almacenesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un almacén para editar");
            return;
        }

        if (modifyAlmacenUseCase == null || findByIdAlmacenUseCase == null) {
            showWarning("No disponible", "Las operaciones de edición no están disponibles");
            return;
        }

        var session = SessionManager.getInstance();
        if (!session.canManageWarehouses()) {
            showError("Acceso denegado", "No tienes permisos para editar almacenes");
            return;
        }

        ModifyAlmacenController dialog = new ModifyAlmacenController(
                modifyAlmacenUseCase,
                findByIdAlmacenUseCase,
                this::loadAlmacenes
        );
        dialog.show(navigator.getStage(), selected.getId());
    }

    private void handleDelete() {
        Almacen selected = almacenesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un almacén para eliminar");
            return;
        }

        var session = SessionManager.getInstance();
        if (!session.canManageWarehouses()) {
            showError("Acceso denegado", "No tienes permisos para eliminar almacenes");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar el almacén '" + selected.getNombre() + "'?");

        var result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = deleteAlmacenUseCase.execute(selected.getId());
        if (success) {
            System.out.println("✓ Almacén eliminado: " + selected.getNombre());
            loadAlmacenes();
        } else {
            showError("Error", "No se pudo eliminar el almacén");
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