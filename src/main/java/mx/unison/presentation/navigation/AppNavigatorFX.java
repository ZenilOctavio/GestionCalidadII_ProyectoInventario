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
 * Navegador para JavaFX
 */
public class AppNavigatorFX {
    public static final String LOGIN = "LOGIN";
    public static final String HOME = "HOME";
    public static final String PRODUCTOS = "PRODUCTOS";
    public static final String ALMACENES = "ALMACENES";
    public static final String USUARIOS = "USUARIOS";

    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();
    private String currentScene;

    public AppNavigatorFX(Stage stage) {
        this.stage = stage;
    }

    public void registerScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void registerSceneWithController(String name, Scene scene, Object controller) {
        scenes.put(name, scene);
        controllers.put(name, controller);
    }

    public void navigateTo(String sceneName) {
        if (!scenes.containsKey(sceneName)) {
            System.err.println("Escena no registrada: " + sceneName);
            return;
        }

        Scene scene = scenes.get(sceneName);
        stage.setScene(scene);
        currentScene = sceneName;
        System.out.println("→ Navegando a: " + sceneName);

        // ✓ Llamar onSceneShown() si el controlador lo implementa
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

    public String getCurrentScene() {
        return currentScene;
    }

    public Stage getStage() {
        return stage;
    }
}