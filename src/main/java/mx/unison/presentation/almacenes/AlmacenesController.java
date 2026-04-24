package mx.unison.presentation.almacenes;

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
import mx.unison.core.domain.models.Almacen;
import mx.unison.presentation.almacenes.dialogs.CreateAlmacenController;
import mx.unison.presentation.almacenes.dialogs.ModifyAlmacenController;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.presentation.utils.FormatterUtil;
import mx.unison.usecases.almacenes.*;

import java.io.IOException;

/**
 * Controlador para la vista de gestión de almacenes.
 * Esta clase permite administrar los puntos de distribución, visualizando su nombre,
 * fecha de creación y el último usuario que realizó modificaciones.
 *
 * En la arquitectura del sistema, actúa como un mediador entre la UI de JavaFX
 * y los casos de uso de gestión de almacenes.
 */
public class AlmacenesController {
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
    private TableView<Almacen> almacenesTable;
    @FXML
    private TableColumn<Almacen, Integer> idColumn;
    @FXML
    private TableColumn<Almacen, String> nombreColumn;
    @FXML
    private TableColumn<Almacen, String> fechaCreacionColumn;
    @FXML
    private TableColumn<Almacen, String> ultimoUsuarioColumn;
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
    private GetAllAlmacenesUseCase getAllAlmacenesUseCase;
    private DeleteAlmacenUseCase deleteAlmacenUseCase;
    private CreateAlmacenUseCase createAlmacenUseCase;
    private ModifyAlmacenUseCase modifyAlmacenUseCase;
    private FindByIdAlmacenUseCase findByIdAlmacenUseCase;
    private boolean initialized = false;

    /**
     * Constructor del controlador de almacenes.
     *
     * @param navigator Navegador de escenas.
     * @param getAllAlmacenesUseCase Caso de uso para obtener todos los almacenes.
     * @param deleteAlmacenUseCase Caso de uso para eliminar un almacén.
     * @param createAlmacenUseCase Caso de uso para crear un almacén.
     * @param modifyAlmacenUseCase Caso de uso para modificar un almacén.
     * @param findByIdAlmacenUseCase Caso de uso para buscar un almacén por ID.
     */
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

    /**
     * Crea e inicializa la escena de almacenes.
     *
     * @return Escena configurada para la gestión de almacenes.
     * @throws RuntimeException Si falla la carga del archivo FXML.
     */
    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/almacenes.fxml"));
            loader.setController(this);
            AnchorPane rootNode = loader.load();
            return new Scene(rootNode, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar almacenes.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Inicialización automática de JavaFX.
     */
    @FXML
    private void initialize() {
        setupStyles();
        buildUI();
        configureTable();
    }

    /**
     * Configura los estilos visuales basados en el tema global.
     */
    private void setupStyles() {
        root.setStyle("-fx-background-color: " + ThemeConfig.Colors.BG_PRIMARY + ";");
        mainContainer.setStyle("-fx-background-color: transparent;");
        
        almacenesTable.setStyle(ThemeConfig.getTableStyle());
        placeholderLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-family: '%s';",
                ThemeConfig.Colors.FG_MUTED,
                ThemeConfig.Typography.FONT_PRIMARY
        ));
    }

    /**
     * Construye los elementos de la UI dinámica.
     */
    private void buildUI() {
        // Título y Subtítulo
        Label titleLabel = ComponentFactory.createTitleLabel("Gestión de Almacenes");
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Control de depósitos y puntos de distribución");
        titleContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Botones de Acción
        createButton = ComponentFactory.createPrimaryButton("+ Nuevo Almacén");
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
     * Configura la visualización de datos en la tabla.
     */
    private void configureTable() {
        idColumn.setCellValueFactory(cellData -> 
                new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        nombreColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getNombre())
        );
        fechaCreacionColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(FormatterUtil.formatDateString(cellData.getValue().getFechaHoraCreacion()))
        );
        ultimoUsuarioColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getUltimoUsuario())
        );

        // Estilo de encabezados
        idColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        nombreColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        fechaCreacionColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        ultimoUsuarioColumn.setStyle(ThemeConfig.getTableHeaderStyle());

        // Resaltado de fila seleccionada
        almacenesTable.setRowFactory(tv -> {
            TableRow<Almacen> row = new TableRow<>();
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

        almacenesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Se ejecuta al mostrar la pantalla. Valida permisos y carga datos.
     */
    public void onSceneShown() {
        applyFadeInAnimation(root);
        
        if (initialized) return;
        initialized = true;

        var session = SessionManager.getInstance();
        if (!session.canManageWarehouses()) {
            showError("Acceso denegado", "No tienes permisos para acceder a esta sección");
            handleBack();
            return;
        }

        loadAlmacenes();
    }

    /**
     * Carga la lista de almacenes desde el repositorio.
     */
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

    /**
     * Abre el diálogo de creación de almacén.
     */
    private void handleCreate() {
        if (createAlmacenUseCase == null) {
            showWarning("No disponible", "Las operaciones de creación no están disponibles");
            return;
        }

        CreateAlmacenController dialog = new CreateAlmacenController(
                createAlmacenUseCase,
                this::loadAlmacenes
        );
        dialog.show(navigator.getStage());
    }

    /**
     * Abre el diálogo de edición para el almacén seleccionado.
     */
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

        ModifyAlmacenController dialog = new ModifyAlmacenController(
                modifyAlmacenUseCase,
                findByIdAlmacenUseCase,
                this::loadAlmacenes
        );
        dialog.show(navigator.getStage(), selected.getId());
    }

    /**
     * Elimina el almacén seleccionado con confirmación previa.
     */
    private void handleDelete() {
        Almacen selected = almacenesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un almacén para eliminar");
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

    /**
     * Navega de regreso al Home.
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