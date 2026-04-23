package mx.unison.presentation.usuarios;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import mx.unison.core.domain.models.Usuario;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.usuarios.dialogs.CreateUsuarioController;
import mx.unison.presentation.usuarios.dialogs.ModifyUsuarioController;
import mx.unison.usecases.usuarios.GetAllUsuariosUseCase;
import mx.unison.usecases.usuarios.DeleteUsuarioUseCase;
import mx.unison.usecases.usuarios.CreateUsuarioUseCase;
import mx.unison.usecases.usuarios.ModifyUsuarioUseCase;

import java.io.IOException;

public class UsuariosController {
    @FXML
    private TableView<Usuario> usuariosTable;
    @FXML
    private TableColumn<Usuario, String> nombreColumn;
    @FXML
    private TableColumn<Usuario, String> rolColumn;
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
    private GetAllUsuariosUseCase getAllUsuariosUseCase;
    private DeleteUsuarioUseCase deleteUsuarioUseCase;
    private CreateUsuarioUseCase createUsuarioUseCase;
    private ModifyUsuarioUseCase modifyUsuarioUseCase;
    private boolean initialized = false;  // ✓ Flag de inicialización

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

    @FXML
    private void initialize() {
        // ✓ NO verificar permisos aquí, solo configurar componentes
        nombreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre())
        );
        rolColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRol())
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
        if (!session.canManageUsers()) {
            showError("Acceso denegado", "Solo administradores pueden gestionar usuarios");
            handleBack();
            return;
        }

        loadUsuarios();
    }

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