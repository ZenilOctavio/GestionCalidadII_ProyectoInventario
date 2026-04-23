package mx.unison.presentation.navigation;

import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.HashMap;
import java.util.Map;

/**
 * Navegador para JavaFX
 * Reemplaza AppNavigator de Swing
 */
public class AppNavigatorFX {
    public static final String LOGIN = "LOGIN";
    public static final String HOME = "HOME";
    public static final String PRODUCTOS = "PRODUCTOS";
    public static final String ALMACENES = "ALMACENES";

    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private String currentScene;

    public AppNavigatorFX(Stage stage) {
        this.stage = stage;
    }

    public void registerScene(String name, Scene scene) {
        scenes.put(name, scene);
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
    }

    public String getCurrentScene() {
        return currentScene;
    }

    public Stage getStage() {
        return stage;
    }
}