package mx.unison.presentation.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label usuarioLabel;
    @FXML
    private Label rolLabel;
    @FXML
    private Button productosButton;
    @FXML
    private Button almacenesButton;
    @FXML
    private Button usuariosButton;
    @FXML
    private Button logoutButton;
    @FXML
    private VBox root;

    private AppNavigatorFX navigator;
    private boolean initialized = false;  // ✓ Flag de inicialización

    public HomeController(AppNavigatorFX navigator) {
        this.navigator = navigator;
    }

    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            loader.setController(this);
            AnchorPane root = loader.load();
            return new Scene(root, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar home.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        // ✓ Solo configurar listeners, NO verificar sesión aquí
        productosButton.setOnAction(e -> handleProductos());
        almacenesButton.setOnAction(e -> handleAlmacenes());
        usuariosButton.setOnAction(e -> handleUsuarios());
        logoutButton.setOnAction(e -> handleLogout());
    }

    // ✓ NUEVO: Método para inicializar cuando se muestra la pantalla
    public void onSceneShown() {
        if (initialized) return;
        initialized = true;

        // ✓ Mostrar información del usuario
        var session = SessionManager.getInstance();
        var usuario = session.getCurrentUser();

        if (usuario != null) {
            usuarioLabel.setText("Usuario: " + usuario.getNombre());
            rolLabel.setText("Rol: " + usuario.getRol());
            System.out.println("✓ Home: Usuario logueado - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        } else {
            System.err.println("⚠ No hay usuario en sesión");
            usuarioLabel.setText("Usuario: No logueado");
            rolLabel.setText("Rol: N/A");
        }

        // ✓ Mostrar/ocultar botones según permisos
        productosButton.setVisible(session.canManageProducts());
        productosButton.setManaged(session.canManageProducts());
        productosButton.setDisable(false);  // ✓ Habilitar botón

        almacenesButton.setVisible(session.canManageWarehouses());
        almacenesButton.setManaged(session.canManageWarehouses());
        almacenesButton.setDisable(false);  // ✓ Habilitar botón

        usuariosButton.setVisible(session.canManageUsers());
        usuariosButton.setManaged(session.canManageUsers());
        usuariosButton.setDisable(false);  // ✓ Habilitar botón

        System.out.println("✓ Permisos asignados:");
        System.out.println("  - Productos: " + session.canManageProducts());
        System.out.println("  - Almacenes: " + session.canManageWarehouses());
        System.out.println("  - Usuarios: " + session.canManageUsers());
    }

    private void handleProductos() {
        System.out.println("→ Navegando a Productos");
        navigator.navigateTo(AppNavigatorFX.PRODUCTOS);
    }

    private void handleAlmacenes() {
        System.out.println("→ Navegando a Almacenes");
        navigator.navigateTo(AppNavigatorFX.ALMACENES);
    }

    private void handleUsuarios() {
        System.out.println("→ Navegando a Usuarios");
        navigator.navigateTo(AppNavigatorFX.USUARIOS);
    }

    private void handleLogout() {
        System.out.println("✓ Cerrando sesión");
        SessionManager.getInstance().logout();
        initialized = false;  // ✓ Resetear flag para próximo login
        navigator.navigateTo(AppNavigatorFX.LOGIN);
    }
}