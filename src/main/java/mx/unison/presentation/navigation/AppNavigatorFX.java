package mx.unison.presentation.navigation;

import javafx.stage.Stage;
import javafx.scene.Scene;
import mx.unison.presentation.home.HomeController;
import mx.unison.presentation.productos.ProductosController;
import mx.unison.presentation.almacenes.AlmacenesController;
import mx.unison.presentation.usuarios.UsuariosController;

import java.util.HashMap;
import java.util.Map;

/**
 * Orquestador de navegación centralizado para la aplicación JavaFX.
 * Se encarga de gestionar el cambio entre las diferentes escenas (Login, Home, Productos, etc.)
 * y de notificar a los controladores correspondientes cuando su vista se vuelve activa.
 * 
 * Implementa un registro de escenas y controladores para facilitar la transición fluida y la
 * ejecución de lógica de inicialización en tiempo real (ej. refrescar datos al entrar a una vista).
 */
public class AppNavigatorFX {
    /** Identificador de la escena de inicio de sesión */
    public static final String LOGIN = "LOGIN";
    /** Identificador de la escena principal (Dashboard) */
    public static final String HOME = "HOME";
    /** Identificador de la sección de gestión de productos */
    public static final String PRODUCTOS = "PRODUCTOS";
    /** Identificador de la sección de gestión de almacenes */
    public static final String ALMACENES = "ALMACENES";
    /** Identificador de la sección de gestión de usuarios */
    public static final String USUARIOS = "USUARIOS";

    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();
    private String currentScene;

    /**
     * Crea una instancia del navegador asociada a la ventana principal.
     * 
     * @param stage El escenario (Stage) principal de la aplicación.
     */
    public AppNavigatorFX(Stage stage) {
        this.stage = stage;
    }

    /**
     * Registra una escena en el navegador sin asociar un controlador explícito.
     * 
     * @param name Nombre identificador de la escena.
     * @param scene El objeto Scene de JavaFX.
     */
    public void registerScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    /**
     * Registra una escena junto con su controlador para permitir notificaciones de eventos.
     * 
     * @param name Nombre identificador de la escena.
     * @param scene El objeto Scene de JavaFX.
     * @param controller El controlador (Controller) asociado a la vista.
     */
    public void registerSceneWithController(String name, Scene scene, Object controller) {
        scenes.put(name, scene);
        controllers.put(name, controller);
    }

    /**
     * Cambia la vista actual de la aplicación por la escena especificada.
     * Si la escena tiene un controlador registrado que implementa la lógica de visualización,
     * se llamará automáticamente al método onSceneShown().
     * 
     * @param sceneName El nombre identificador de la escena a la cual navegar.
     */
    public void navigateTo(String sceneName) {
        if (!scenes.containsKey(sceneName)) {
            System.err.println("Escena no registrada: " + sceneName);
            return;
        }

        Scene scene = scenes.get(sceneName);
        stage.setScene(scene);
        currentScene = sceneName;
        System.out.println("→ Navegando a: " + sceneName);

        // Notificar al controlador si es necesario (Polimorfismo manual para disparar onSceneShown)
        Object controller = controllers.get(sceneName);
        if (controller instanceof HomeController) {
            ((HomeController) controller).onSceneShown();
        } else if (controller instanceof ProductosController) {
            ((ProductosController) controller).onSceneShown();
        } else if (controller instanceof AlmacenesController) {
            ((AlmacenesController) controller).onSceneShown();
        } else if (controller instanceof UsuariosController) {
            ((UsuariosController) controller).onSceneShown();
        }
    }

    /**
     * @return El nombre identificador de la escena que se muestra actualmente.
     */
    public String getCurrentScene() {
        return currentScene;
    }

    /**
     * @return El escenario principal (Stage).
     */
    public Stage getStage() {
        return stage;
    }
}