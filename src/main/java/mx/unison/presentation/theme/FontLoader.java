package mx.unison.presentation.theme;

import javafx.scene.text.Font;
import java.io.InputStream;

/**
 * Cargador de fuentes personalizado con fallback automático
 */
/**
 * Cargador de fuentes personalizado para la aplicación.
 * Gestiona la carga de archivos de fuentes TrueType (.ttf) desde los recursos
 * y proporciona métodos para obtener instancias de {@link Font} con fallbacks automáticos.
 */
public class FontLoader {

    private static boolean jetbrainsMonoLoaded = false;
    private static boolean robotoMonoLoaded = false;

    static {
        loadFonts();
    }

    /**
     * Intenta cargar las fuentes JetBrains Mono y Roboto Mono desde el classpath.
     * Si una fuente no puede ser cargada, se registra el error y se utiliza un fallback.
     */
    private static void loadFonts() {
        // Intentar cargar JetBrains Mono
        try {
            InputStream fontStream = FontLoader.class.getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf");
            if (fontStream != null) {
                Font.loadFont(fontStream, 13);
                jetbrainsMonoLoaded = true;
                System.out.println("✓ Fuente JetBrains Mono cargada correctamente");
            } else {
                System.out.println("⚠ Fuente JetBrains Mono no encontrada en recursos, usando fallback");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error al cargar JetBrains Mono: " + e.getMessage() + ", usando fallback");
        }

        // Intentar cargar Roboto Mono como fallback
        try {
            InputStream fontStream = FontLoader.class.getResourceAsStream("/fonts/RobotoMono-Regular.ttf");
            if (fontStream != null) {
                Font.loadFont(fontStream, 13);
                robotoMonoLoaded = true;
                System.out.println("✓ Fuente Roboto Mono cargada como fallback");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error al cargar Roboto Mono: " + e.getMessage());
        }
    }

    /**
     * Obtiene una fuente con el tamaño especificado.
     * Prioriza JetBrains Mono, luego Roboto Mono y finalmente Monospaced.
     *
     * @param size El tamaño de la fuente en puntos.
     * @return Una instancia de {@link Font}.
     */
    public static Font getFont(int size) {
        if (jetbrainsMonoLoaded) {
            return Font.font("JetBrains Mono", size);
        } else if (robotoMonoLoaded) {
            return Font.font("Roboto Mono", size);
        } else {
            return Font.font("Monospaced", size);
        }
    }

    /**
     * Obtiene una fuente con peso y tamaño especificados.
     * Actualmente delega a {@link #getFont(int)} ya que la familia de la fuente
     * ya define su peso base.
     *
     * @param size El tamaño de la fuente.
     * @param weight El peso de la fuente (ej. "700").
     * @return Una instancia de {@link Font}.
     */
    public static Font getFont(int size, String weight) {
        if (jetbrainsMonoLoaded) {
            return Font.font("JetBrains Mono", size);
        } else if (robotoMonoLoaded) {
            return Font.font("Roboto Mono", size);
        } else {
            return Font.font("Monospaced", size);
        }
    }

    /**
     * Obtiene el nombre de la familia de la fuente actual que se ha cargado con éxito.
     *
     * @return El nombre de la familia (ej. "JetBrains Mono").
     */
    public static String getCurrentFontFamily() {
        if (jetbrainsMonoLoaded) {
            return "JetBrains Mono";
        } else if (robotoMonoLoaded) {
            return "Roboto Mono";
        } else {
            return "Monospaced";
        }
    }

    /**
     * Verifica si la fuente principal (JetBrains Mono) se cargó correctamente.
     *
     * @return true si la fuente principal está disponible, false de lo contrario.
     */
    public static boolean isPrimaryFontLoaded() {
        return jetbrainsMonoLoaded;
    }
}