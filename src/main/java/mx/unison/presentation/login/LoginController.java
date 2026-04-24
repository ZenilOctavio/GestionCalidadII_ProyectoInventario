package mx.unison.presentation.login;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;
import mx.unison.usecases.LoginUseCase;

import java.io.IOException;

/**
 * Controlador para la vista de inicio de sesión (Login).
 * Esta clase gestiona la autenticación de usuarios, la validación de credenciales
 * y la construcción dinámica de la interfaz de usuario de acceso.
 *
 * En la arquitectura JavaFX, actúa como el controlador para el archivo FXML de login,
 * interactuando con {@link LoginUseCase} para la lógica de negocio.
 */
public class LoginController {
    @FXML
    private VBox loginContainer;
    @FXML
    private VBox formFields;
    @FXML
    private VBox buttonContainer;
    @FXML
    private Label footerLabel;
    @FXML
    private AnchorPane root;

    private LoginUseCase loginUseCase;
    private AppNavigatorFX navigator;

    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    private boolean initialized = false;  // ✓ Flag de inicialización

    /**
     * Constructor del controlador de login.
     *
     * @param loginUseCase El caso de uso para gestionar el proceso de autenticación.
     * @param navigator El navegador para gestionar las transiciones de escena.
     */
    public LoginController(LoginUseCase loginUseCase, AppNavigatorFX navigator) {
        this.loginUseCase = loginUseCase;
        this.navigator = navigator;
    }

    /**
     * Crea e inicializa la escena de login.
     *
     * @return Una nueva instancia de Scene para el inicio de sesión.
     * @throws RuntimeException Si ocurre un error al cargar el archivo FXML.
     */
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

    /**
     * Método de inicialización de JavaFX.
     * Llama a la construcción dinámica de la interfaz.
     */
    @FXML
    private void initialize() {
        buildUI();
    }

    /**
     * Método invocado cuando la escena de login se muestra en pantalla.
     * Resetea el estado del formulario y aplica animaciones.
     */
    public void onSceneShown() {
        if (initialized) {
            // Resetear opacidad si volvemos del logout
            root.setOpacity(1.0);
            applyFadeInAnimation(root);

            // Limpiar campos
            usernameField.clear();
            passwordField.clear();
            errorLabel.setVisible(false);
            usernameField.requestFocus();

            System.out.println("✓ Login mostrado nuevamente");
            return;
        }

        initialized = true;
        applyFadeInAnimation(root);
    }

    /**
     * Construye la interfaz de usuario de forma dinámica utilizando {@link ComponentFactory}.
     * Configura títulos, campos de entrada, botones y sus respectivos estilos.
     */
    private void buildUI() {
        // Título principal
        Label titleLabel = ComponentFactory.createTitleLabel("Bienvenido");

        // Subtítulo
        Label subtitleLabel = ComponentFactory.createSubtitleLabel("Ingresa tus credenciales para continuar");

        // Agregar título y subtítulo
        VBox headerBox = new VBox();
        headerBox.setSpacing(ThemeConfig.Spacing.SM);
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        loginContainer.getChildren().add(0, headerBox);

        // Crear campos del formulario
        usernameField = ComponentFactory.createStyledTextField("Nombre de usuario");
        passwordField = ComponentFactory.createStyledPasswordField("Contraseña");

        VBox usuarioGroup = ComponentFactory.createFormFieldGroup("Usuario", usernameField);
        VBox passwordGroup = ComponentFactory.createFormFieldGroup("Contraseña", passwordField);

        formFields.getChildren().addAll(usuarioGroup, passwordGroup);

        // Crear label de error (inicialmente invisible)
        errorLabel = ComponentFactory.createErrorLabel("");
        errorLabel.setVisible(false);
        formFields.getChildren().add(errorLabel);

        // Crear botón de login
        Button loginButton = ComponentFactory.createPrimaryButton("Iniciar Sesión");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> handleLogin());

        // Agregar efecto hover con transición suave
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(ThemeConfig.getPrimaryButtonHoverStyle());
            loginButton.setPrefWidth(Double.MAX_VALUE);
        });
        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(ThemeConfig.getPrimaryButtonStyle());
            loginButton.setPrefWidth(Double.MAX_VALUE);
        });

        buttonContainer.getChildren().add(loginButton);

        // Permitir login con Enter
        usernameField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                handleLogin();
            }
        });
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                handleLogin();
            }
        });

        // Estilizar footer
        footerLabel.setStyle(String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-text-fill: %s;",
                ThemeConfig.Typography.FONT_PRIMARY,
                ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_SM,
                ThemeConfig.Colors.FG_MUTED
        ));

        System.out.println("✓ UI de Login construida");
        System.out.println("  - Fuente: " + FontLoader.getCurrentFontFamily());
        System.out.println("  - Primaria disponible: " + FontLoader.isPrimaryFontLoaded());
    }

    /**
     * Maneja la acción del botón de inicio de sesión.
     * Valida las entradas, intenta autenticar al usuario y gestiona la navegación al Home.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Limpiar error previo
        errorLabel.setVisible(false);

        if (username.isEmpty()) {
            showError("Por favor ingresa tu nombre de usuario");
            return;
        }

        if (password.isEmpty()) {
            showError("Por favor ingresa tu contraseña");
            return;
        }

        var usuarioOpt = loginUseCase.withUsernamePassword(username, password);

        if (usuarioOpt.isEmpty()) {
            showError("Usuario o contraseña incorrectos");
            passwordField.clear();
            return;
        }

        var usuario = usuarioOpt.get();
        SessionManager.getInstance().login(usuario);

        System.out.println("✓ Login exitoso para usuario: " + username + " - Rol: " + usuario.getRol());

        // Aplicar fade out antes de navegar
        applyFadeOutAnimation(() -> navigator.navigateTo(AppNavigatorFX.HOME));
    }

    /**
     * Muestra un mensaje de error en la interfaz con una animación de desvanecimiento.
     *
     * @param message El mensaje de error a mostrar.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        // Animar la aparición del error
        FadeTransition fade = new FadeTransition(Duration.millis(300), errorLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Aplica una animación de desvanecimiento de entrada (Fade In).
     *
     * @param node El nodo al que se aplicará la animación.
     */
    private void applyFadeInAnimation(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Aplica una animación de desvanecimiento de salida (Fade Out) antes de ejecutar una acción.
     *
     * @param onFinished La acción a ejecutar una vez finalizada la animación.
     */
    private void applyFadeOutAnimation(Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), root);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> onFinished.run());
        fade.play();
    }
}