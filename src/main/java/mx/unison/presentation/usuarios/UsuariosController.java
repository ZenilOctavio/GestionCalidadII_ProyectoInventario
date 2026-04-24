package mx.unison.presentation.usuarios;

import javafx.animation.FadeTransition;
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
import mx.unison.core.domain.models.Usuario;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.presentation.usuarios.dialogs.CreateUsuarioController;
import mx.unison.presentation.usuarios.dialogs.ModifyUsuarioController;
import mx.unison.usecases.usuarios.CreateUsuarioUseCase;
import mx.unison.usecases.usuarios.DeleteUsuarioUseCase;
import mx.unison.usecases.usuarios.GetAllUsuariosUseCase;
import mx.unison.usecases.usuarios.ModifyUsuarioUseCase;

import java.io.IOException;

/**
 * Controlador para la vista de gestión de usuarios.
 * Permite a los administradores visualizar, crear, editar y eliminar cuentas de usuario.
 *
 * Esta clase gestiona una tabla de datos (TableView) y coordina con diversos casos de uso
 * para realizar operaciones CRUD sobre el modelo {@link Usuario}.
 */
public class UsuariosController {
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
    private TableView<Usuario> usuariosTable;
    @FXML
    private TableColumn<Usuario, String> nombreColumn;
    @FXML
    private TableColumn<Usuario, String> rolColumn;
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
    private GetAllUsuariosUseCase getAllUsuariosUseCase;
    private DeleteUsuarioUseCase deleteUsuarioUseCase;
    private CreateUsuarioUseCase createUsuarioUseCase;
    private ModifyUsuarioUseCase modifyUsuarioUseCase;
    private boolean initialized = false;

    /**
     * Constructor del controlador de usuarios.
     *
     * @param navigator Navegador para gestionar transiciones entre pantallas.
     * @param getAllUsuariosUseCase Caso de uso para obtener la lista de usuarios.
     * @param deleteUsuarioUseCase Caso de uso para eliminar un usuario.
     * @param createUsuarioUseCase Caso de uso para crear un nuevo usuario.
     * @param modifyUsuarioUseCase Caso de uso para modificar un usuario existente.
     */
    public UsuariosController(AppNavigatorFX navigator,
                              GetAllUsuariosUseCase getAllUsuariosUseCase,
                              DeleteUsuarioUseCase deleteUsuarioUseCase,
                              CreateUsuarioUseCase createUsuarioUseCase,
                              ModifyUsuarioUseCase modifyUsuarioUseCase) {
        this.navigator = navigator;
        this.getAllUsuariosUseCase = getAllUsuariosUseCase;
        this.deleteUsuarioUseCase = deleteUsuarioUseCase;
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.modifyUsuarioUseCase = modifyUsuarioUseCase;
    }

    /**
     * Crea e inicializa la escena de gestión de usuarios.
     *
     * @return Una instancia de Scene configurada para esta vista.
     * @throws RuntimeException Si ocurre un error al cargar el archivo FXML.
     */
    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/usuarios.fxml"));
            loader.setController(this);
            AnchorPane root = loader.load();
            return new Scene(root, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Inicialización automática de JavaFX.
     * Configura estilos, construye la UI dinámica y configura la tabla.
     */
    @FXML
    private void initialize() {
        setupStyles();
        buildUI();
        configureTable();
    }

    /**
     * Aplica estilos visuales a los componentes de la vista.
     */
    private void setupStyles() {
        root.setStyle("-fx-background-color: " + ThemeConfig.Colors.BG_PRIMARY + ";");
        mainContainer.setStyle("-fx-background-color: transparent;");
        
        // Estilo para la tabla
        usuariosTable.setStyle(ThemeConfig.getTableStyle());
        nombreColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        rolColumn.setStyle(ThemeConfig.getTableHeaderStyle());
        
        placeholderLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-family: '%s';",
                ThemeConfig.Colors.FG_MUTED,
                ThemeConfig.Typography.FONT_PRIMARY
        ));
    }

    /**
     * Construye elementos de la interfaz de usuario de forma dinámica.
     */
    private void buildUI() {
        // Título y Subtítulo
        Label titleLabel = ComponentFactory.createTitleLabel("Gestión de Usuarios");
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Administra los accesos y roles del personal");
        titleContainer.getChildren().addAll(titleLabel, subtitleLabel);

        // Botones de Acción
        createButton = ComponentFactory.createPrimaryButton("+ Nuevo Usuario");
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
        
        // Hover effects para backButton
        backButton.setOnMouseEntered(e -> backButton.setStyle(backButtonStyle + "-fx-text-fill: " + ThemeConfig.Colors.EMERALD_400 + ";"));
        backButton.setOnMouseExited(e -> backButton.setStyle(backButtonStyle));
    }

    /**
     * Configura las columnas y el comportamiento de la tabla de usuarios.
     */
    private void configureTable() {
        nombreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre())
        );
        rolColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRol())
        );

        // Resaltado de fila seleccionada
        usuariosTable.setRowFactory(tv -> {
            TableRow<Usuario> row = new TableRow<>();
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

        // Ajustar columnas
        usuariosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Se ejecuta cuando la escena se vuelve visible.
     * Verifica permisos y carga los datos de la tabla.
     */
    public void onSceneShown() {
        applyFadeInAnimation(root);
        
        if (initialized) return;
        initialized = true;

        var session = SessionManager.getInstance();
        if (!session.canManageUsers()) {
            showError("Acceso denegado", "Solo administradores pueden gestionar usuarios");
            handleBack();
            return;
        }

        loadUsuarios();
    }

    /**
     * Carga la lista de usuarios desde la base de datos y la muestra en la tabla.
     */
    private void loadUsuarios() {
        try {
            var usuarios = getAllUsuariosUseCase.execute();
            ObservableList<Usuario> observableList = FXCollections.observableArrayList(usuarios);
            usuariosTable.setItems(observableList);
            System.out.println("✓ Usuarios cargados: " + usuarios.size());
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    /**
     * Maneja la acción de crear un nuevo usuario abriendo un diálogo.
     */
    private void handleCreate() {
        var session = SessionManager.getInstance();
        if (!session.canManageUsers()) {
            showError("Acceso denegado", "No tienes permisos para crear usuarios");
            return;
        }

        CreateUsuarioController dialog = new CreateUsuarioController(
                createUsuarioUseCase,
                this::loadUsuarios
        );
        dialog.show(navigator.getStage());
    }

    /**
     * Maneja la acción de editar el usuario seleccionado.
     */
    private void handleEdit() {
        Usuario selected = usuariosTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un usuario para editar");
            return;
        }

        var session = SessionManager.getInstance();
        if (!session.canManageUsers()) {
            showError("Acceso denegado", "No tienes permisos para editar usuarios");
            return;
        }

        ModifyUsuarioController dialog = new ModifyUsuarioController(
                modifyUsuarioUseCase,
                this::loadUsuarios
        );
        dialog.show(navigator.getStage(), selected.getNombre());
    }

    /**
     * Maneja la acción de eliminar el usuario seleccionado tras una confirmación.
     */
    private void handleDelete() {
        Usuario selected = usuariosTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selección vacía", "Por favor selecciona un usuario para eliminar");
            return;
        }

        var session = SessionManager.getInstance();
        if (selected.getNombre().equals(session.getCurrentUser().getNombre())) {
            showError("Error", "No puedes eliminarte a ti mismo");
            return;
        }

        if (!session.canManageUsers()) {
            showError("Acceso denegado", "No tienes permisos para eliminar usuarios");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar el usuario '" + selected.getNombre() + "'?");
        
        var result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = deleteUsuarioUseCase.execute(selected);
        if (success) {
            System.out.println("✓ Usuario eliminado: " + selected.getNombre());
            loadUsuarios();
        } else {
            showError("Error", "No se pudo eliminar el usuario");
        }
    }

    /**
     * Navega de vuelta al Home.
     */
    private void handleBack() {
        applyFadeOutAnimation(() -> navigator.navigateTo(AppNavigatorFX.HOME));
    }

    /**
     * Muestra una alerta de advertencia.
     * @param title Título de la alerta.
     * @param message Mensaje de la alerta.
     */
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de error.
     * @param title Título de la alerta.
     * @param message Mensaje de la alerta.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Aplica animación de Fade In.
     * @param node Nodo a animar.
     */
    private void applyFadeInAnimation(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Aplica animación de Fade Out antes de ejecutar una acción.
     * @param onFinished Acción a ejecutar al finalizar.
     */
    private void applyFadeOutAnimation(Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), root);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> onFinished.run());
        fade.play();
    }
}