package mx.unison.presentation.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import mx.unison.presentation.navigation.AppNavigatorFX;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button productosButton;
    @FXML
    private Button almacenesButton;
    @FXML
    private Button logoutButton;
    @FXML
    private VBox root;

    private AppNavigatorFX navigator;

    public HomeController(AppNavigatorFX navigator) {
        this.navigator = navigator;
    }

    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            loader.setController(this);
            VBox root = loader.load();  // ✓ Correcto: VBox
            return new Scene(root, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar home.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        productosButton.setOnAction(e -> handleProductos());
        almacenesButton.setOnAction(e -> handleAlmacenes());
        logoutButton.setOnAction(e -> handleLogout());
    }

    private void handleProductos() {
        System.out.println("→ Navegando a Productos");
        navigator.navigateTo(AppNavigatorFX.PRODUCTOS);
    }

    private void handleAlmacenes() {
        System.out.println("→ Navegando a Almacenes");
        navigator.navigateTo(AppNavigatorFX.ALMACENES);
    }

    private void handleLogout() {
        System.out.println("✓ Cerrando sesión");
        navigator.navigateTo(AppNavigatorFX.LOGIN);
    }
}