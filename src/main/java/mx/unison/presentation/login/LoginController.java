package mx.unison.presentation.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import mx.unison.usecases.LoginUseCase;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane root;

    private LoginUseCase loginUseCase;
    private AppNavigatorFX navigator;

    public LoginController(LoginUseCase loginUseCase, AppNavigatorFX navigator) {
        this.loginUseCase = loginUseCase;
        this.navigator = navigator;
    }

    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setController(this);
            AnchorPane root = loader.load();
            return new Scene(root, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar login.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        usernameField.setStyle("-fx-font-size: 14;");
        passwordField.setStyle("-fx-font-size: 14;");
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty()) {
            showError("Usuario vacío", "Por favor ingrese su usuario");
            return;
        }

        if (password.isEmpty()) {
            showError("Contraseña vacía", "Por favor ingrese su contraseña");
            return;
        }

        var usuarioOpt = loginUseCase.withUsernamePassword(username, password);

        if (usuarioOpt.isEmpty()) {
            showError("Credenciales inválidas", "El usuario o contraseña son incorrectos");
            passwordField.clear();
            return;
        }

        // ✓ Guardar usuario en sesión
        var usuario = usuarioOpt.get();
        SessionManager.getInstance().login(usuario);

        System.out.println("✓ Login exitoso para usuario: " + username + " - Rol: " + usuario.getRol());
        navigator.navigateTo(AppNavigatorFX.HOME);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}