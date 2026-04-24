package mx.unison.presentation.home;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import mx.unison.presentation.components.ComponentFactory;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.session.SessionManager;
import mx.unison.presentation.theme.ThemeConfig;

import java.io.IOException;

/**
 * Controlador para la vista principal (Home) de la aplicación.
 * Esta clase gestiona el panel de control donde los usuarios pueden navegar a diferentes
 * secciones del sistema según sus permisos.
 *
 * En la arquitectura JavaFX, actúa como el controlador vinculado al archivo FXML correspondiente,
 * manejando la lógica de presentación, animaciones de transición y eventos de usuario.
 */
public class HomeController {
    @FXML
    private AnchorPane root;
    @FXML
    private VBox mainContainer;
    @FXML
    private HBox headerBar;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label usuarioLabel;
    @FXML
    private Label rolLabel;
    @FXML
    private Button logoutButton;
    
    @FXML
    private Label sectionTitle;
    @FXML
    private VBox productosCard;
    @FXML
    private StackPane productosIconContainer;
    @FXML
    private Label productosTitle;
    @FXML
    private Label productosDesc;
    @FXML
    private Button productosButton;
    
    @FXML
    private VBox almacenesCard;
    @FXML
    private StackPane almacenesIconContainer;
    @FXML
    private Label almacenesTitle;
    @FXML
    private Label almacenesDesc;
    @FXML
    private Button almacenesButton;
    
    @FXML
    private VBox usuariosCard;
    @FXML
    private StackPane usuariosIconContainer;
    @FXML
    private Label usuariosTitle;
    @FXML
    private Label usuariosDesc;
    @FXML
    private Button usuariosButton;
    
    @FXML
    private Label footerLabel;

    private AppNavigatorFX navigator;
    private boolean initialized = false;

    /**
     * Path SVG para el icono de Productos (representa una caja/paquete).
     */
    private static final String ICON_PRODUCTS = "M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4";

    /**
     * Path SVG para el icono de Almacenes (representa un edificio de almacén).
     */
    private static final String ICON_WAREHOUSE = "M3 21h18M3 7v1h18V7l-9-4-9 4zm2 14V8h14v13M7 11h2v2H7v-2zm4 0h2v2h-2v-2zm4 0h2v2h-2v-2zm-8 4h2v2H7v-2zm4 0h2v2h-2v-2zm4 0h2v2h-2v-2z";

    /**
     * Path SVG para el icono de Usuarios (representa siluetas de personas).
     */
    private static final String ICON_USERS = "M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2m16-10a4 4 0 11-8 0 4 4 0 018 0zm6 11v-2a4 4 0 00-3-3.87m-4-12a4 4 0 010 7.75";

    /**
     * Constructor del controlador.
     *
     * @param navigator El navegador de la aplicación para gestionar los cambios de escena.
     */
    public HomeController(AppNavigatorFX navigator) {
        this.navigator = navigator;
    }

    /**
     * Crea e inicializa la escena asociada a este controlador.
     * Carga el archivo FXML y establece este objeto como su controlador.
     *
     * @return Una nueva instancia de Scene configurada para el Home.
     * @throws RuntimeException Si ocurre un error al cargar el archivo FXML.
     */
    public Scene createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            loader.setController(this);
            AnchorPane rootNode = loader.load();
            return new Scene(rootNode, 1000, 720);
        } catch (IOException e) {
            System.err.println("Error al cargar home.fxml: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Método de inicialización automática de JavaFX.
     * Configura estilos, iconos y acciones de los componentes.
     */
    @FXML
    private void initialize() {
        setupStyles();
        setupIcons();
        setupActions();
    }

    /**
     * Configura los estilos CSS de los componentes de la interfaz.
     * Aplica los colores y tipografías definidos en {@link ThemeConfig}.
     */
    private void setupStyles() {
        root.setStyle("-fx-background-color: " + ThemeConfig.Colors.BG_TERTIARY + ";");
        mainContainer.setStyle("-fx-background-color: transparent;");
        
        // Header Bar
        headerBar.setStyle(String.format(
            "-fx-background-color: %s; -fx-border-color: %s; -fx-border-width: 0 0 1 0;",
            ThemeConfig.Colors.BG_PRIMARY, ThemeConfig.Colors.BORDER_DEFAULT
        ));
        
        welcomeLabel.setStyle(ThemeConfig.getLabelStyle() + "-fx-font-size: 20px; -fx-font-weight: bold;");
        dateLabel.setStyle("-fx-text-fill: " + ThemeConfig.Colors.FG_MUTED + "; -fx-font-size: 13px;");
        usuarioLabel.setStyle("-fx-text-fill: " + ThemeConfig.Colors.FG_PRIMARY + "; -fx-font-weight: bold;");
        rolLabel.setStyle("-fx-text-fill: " + ThemeConfig.Colors.EMERALD_600 + "; -fx-font-size: 11px; -fx-font-weight: bold;");
        
        logoutButton.setStyle(String.format(
            "-fx-background-color: transparent; -fx-text-fill: %s; -fx-border-color: %s; -fx-border-radius: 4px; -fx-cursor: hand;",
            ThemeConfig.Colors.ERROR, ThemeConfig.Colors.ERROR
        ));

        // Cards
        styleCard(productosCard, productosTitle, productosDesc, productosButton);
        styleCard(almacenesCard, almacenesTitle, almacenesDesc, almacenesButton);
        styleCard(usuariosCard, usuariosTitle, usuariosDesc, usuariosButton);
        
        sectionTitle.setStyle(ThemeConfig.getLabelStyle() + "-fx-font-size: 24px; -fx-font-weight: bold;");
        footerLabel.setStyle("-fx-text-fill: " + ThemeConfig.Colors.FG_MUTED + "; -fx-font-size: 11px;");
    }

    /**
     * Aplica estilos y efectos visuales a una tarjeta de sección.
     * Configura el comportamiento de hover (escalado y sombras).
     *
     * @param card El contenedor VBox de la tarjeta.
     * @param title La etiqueta de título.
     * @param desc La etiqueta de descripción.
     * @param btn El botón de acción.
     */
    private void styleCard(VBox card, Label title, Label desc, Button btn) {
        card.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 12px; -fx-border-color: %s; -fx-border-radius: 12px; -fx-border-width: 1px;",
            ThemeConfig.Colors.BG_PRIMARY, ThemeConfig.Colors.BORDER_DEFAULT
        ));
        
        title.setStyle(ThemeConfig.getLabelStyle() + "-fx-font-size: 18px; -fx-font-weight: bold;");
        desc.setStyle("-fx-text-fill: " + ThemeConfig.Colors.FG_SECONDARY + "; -fx-font-size: 13px;");
        
        btn.setStyle(ThemeConfig.getPrimaryButtonStyle());
        btn.setPrefWidth(120);

        // Hover Effects for Card
        card.setOnMouseEntered(e -> {
            card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 12px; -fx-border-color: %s; -fx-border-radius: 12px; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 10);",
                ThemeConfig.Colors.BG_PRIMARY, ThemeConfig.Colors.EMERALD_400
            ));
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 12px; -fx-border-color: %s; -fx-border-radius: 12px; -fx-border-width: 1px;",
                ThemeConfig.Colors.BG_PRIMARY, ThemeConfig.Colors.BORDER_DEFAULT
            ));
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    /**
     * Inicializa y agrega los iconos SVG a sus respectivos contenedores.
     */
    private void setupIcons() {
        productosIconContainer.getChildren().add(createIcon(ICON_PRODUCTS, ThemeConfig.Colors.EMERALD_500));
        almacenesIconContainer.getChildren().add(createIcon(ICON_WAREHOUSE, ThemeConfig.Colors.INFO));
        usuariosIconContainer.getChildren().add(createIcon(ICON_USERS, ThemeConfig.Colors.WARNING));
    }

    /**
     * Crea un objeto SVGPath configurado a partir de una cadena de datos y un color.
     *
     * @param pathData La cadena de datos del path SVG.
     * @param color El color web para el trazo del icono.
     * @return Un objeto SVGPath listo para ser mostrado.
     */
    private SVGPath createIcon(String pathData, String color) {
        SVGPath svg = new SVGPath();
        svg.setContent(pathData);
        svg.setFill(Color.TRANSPARENT);
        svg.setStroke(Color.web(color));
        svg.setStrokeWidth(2.0);
        svg.setScaleX(2.5);
        svg.setScaleY(2.5);
        return svg;
    }

    /**
     * Configura los controladores de eventos para los botones de navegación y cierre de sesión.
     */
    private void setupActions() {
        productosButton.setOnAction(e -> handleProductos());
        almacenesButton.setOnAction(e -> handleAlmacenes());
        usuariosButton.setOnAction(e -> handleUsuarios());
        logoutButton.setOnAction(e -> handleLogout());
    }

    /**
     * Método que debe llamarse cuando la escena se vuelve visible.
     * Actualiza la información del usuario en sesión y gestiona la visibilidad
     * de las tarjetas según los permisos del rol.
     */
    public void onSceneShown() {
        applyFadeInAnimation(root);
        
        var session = SessionManager.getInstance();
        var usuario = session.getCurrentUser();

        if (usuario != null) {
            usuarioLabel.setText(usuario.getNombre());
            rolLabel.setText(usuario.getRol().toUpperCase());
        }

        // Mostrar/ocultar según permisos
        productosCard.setVisible(session.canManageProducts());
        productosCard.setManaged(session.canManageProducts());
        
        almacenesCard.setVisible(session.canManageWarehouses());
        almacenesCard.setManaged(session.canManageWarehouses());
        
        usuariosCard.setVisible(session.canManageUsers());
        usuariosCard.setManaged(session.canManageUsers());
    }

    /**
     * Maneja la navegación hacia la sección de productos.
     */
    private void handleProductos() {
        applyFadeOutAnimation(() -> navigator.navigateTo(AppNavigatorFX.PRODUCTOS));
    }

    /**
     * Maneja la navegación hacia la sección de almacenes.
     */
    private void handleAlmacenes() {
        applyFadeOutAnimation(() -> navigator.navigateTo(AppNavigatorFX.ALMACENES));
    }

    /**
     * Maneja la navegación hacia la sección de usuarios.
     */
    private void handleUsuarios() {
        applyFadeOutAnimation(() -> navigator.navigateTo(AppNavigatorFX.USUARIOS));
    }

    /**
     * Maneja el proceso de cierre de sesión, limpiando la sesión y navegando al Login.
     */
    private void handleLogout() {
        applyFadeOutAnimation(() -> {
            SessionManager.getInstance().logout();
            initialized = false;
            navigator.navigateTo(AppNavigatorFX.LOGIN);
        });
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