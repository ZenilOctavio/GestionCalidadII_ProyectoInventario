package mx.unison.presentation.theme;

/**
 * Configuración centralizada de tema y estilos
 * Inspirado en shadcn - Variante Lyra con tema claro (Light Myst)
 */
/**
 * Configuración centralizada de tema y estilos para la aplicación.
 * Proporciona una paleta de colores, tipografía, espaciado y estilos CSS predefinidos
 * para asegurar una apariencia consistente en toda la interfaz de usuario.
 *
 * Inspirado en el sistema de diseño shadcn, utilizando una variante clara (Light Myst).
 */
public class ThemeConfig {

    /**
     * Define los modos de tema disponibles (Claro/Oscuro).
     */
    public enum ThemeMode {
        LIGHT,
        DARK
    }

    private static ThemeMode currentMode = ThemeMode.LIGHT;  // ✓ Cambiado a LIGHT

    /**
     * Establece el modo de tema actual.
     * @param mode El modo de tema a aplicar.
     */
    public static void setThemeMode(ThemeMode mode) {
        currentMode = mode;
    }

    /**
     * Obtiene el modo de tema actual.
     * @return El modo de tema activo.
     */
    public static ThemeMode getThemeMode() {
        return currentMode;
    }

    /**
     * Contiene las constantes de colores utilizadas en la aplicación.
     * Utiliza una paleta basada en tonos pizarra (Slate) y esmeralda (Emerald).
     */
    public static class Colors {
        // Background (Claro)
        public static final String BG_PRIMARY = "#ffffff";      // Blanco puro
        public static final String BG_SECONDARY = "#f8fafc";    // Slate 50
        public static final String BG_TERTIARY = "#f1f5f9";     // Slate 100
        public static final String BG_HOVER = "#e2e8f0";        // Slate 200

        // Foreground (Oscuro para contraste)
        public static final String FG_PRIMARY = "#0f172a";      // Slate 900
        public static final String FG_SECONDARY = "#475569";    // Slate 600
        public static final String FG_TERTIARY = "#64748b";     // Slate 500
        public static final String FG_MUTED = "#94a3b8";        // Slate 400

        // Emerald - Theme principal
        public static final String EMERALD_50 = "#f0fdf4";
        public static final String EMERALD_100 = "#dcfce7";
        public static final String EMERALD_200 = "#bbf7d0";
        public static final String EMERALD_300 = "#86efac";
        public static final String EMERALD_400 = "#4ade80";
        public static final String EMERALD_500 = "#10b981";     // Color primario
        public static final String EMERALD_600 = "#059669";     // Color primario hover
        public static final String EMERALD_700 = "#047857";
        public static final String EMERALD_800 = "#065f46";
        public static final String EMERALD_900 = "#064e3b";

        // Estados
        public static final String SUCCESS = "#10b981";
        public static final String WARNING = "#f59e0b";
        public static final String ERROR = "#ef4444";
        public static final String INFO = "#3b82f6";

        // Bordes
        public static final String BORDER_LIGHT = "#f1f5f9";
        public static final String BORDER_DEFAULT = "#e2e8f0";  // Slate 200
        public static final String BORDER_DARK = "#cbd5e1";     // Slate 300

        // Sombras
        public static final String SHADOW_COLOR = "rgba(0, 0, 0, 0.1)";
    }

    /**
     * Define la tipografía, incluyendo familias de fuentes y tamaños escalonados.
     */
    public static class Typography {
        // Fuentes
        public static final String FONT_PRIMARY = "JetBrains Mono";
        public static final String FONT_FALLBACK = "Roboto Mono";

        // Tamaños
        public static final int SIZE_XS = 11;
        public static final int SIZE_SM = 12;
        public static final int SIZE_BASE = 13;
        public static final int SIZE_LG = 14;
        public static final int SIZE_XL = 16;
        public static final int SIZE_2XL = 18;
        public static final int SIZE_3XL = 20;
        public static final int SIZE_4XL = 24;

        // Pesos (como Strings para CSS)
        public static final String WEIGHT_LIGHT = "300";
        public static final String WEIGHT_NORMAL = "400";
        public static final String WEIGHT_MEDIUM = "500";
        public static final String WEIGHT_SEMIBOLD = "600";
        public static final String WEIGHT_BOLD = "700";
    }

    /**
     * Constantes de espaciado (padding/gap) para mantener consistencia visual.
     */
    public static class Spacing {
        public static final int XS = 4;
        public static final int SM = 8;
        public static final int MD = 12;
        public static final int LG = 16;
        public static final int XL = 24;
        public static final int XXL = 32;
    }

    /**
     * Constantes para radios y anchos de borde.
     */
    public static class Borders {
        public static final String RADIUS_NONE = "0";
        public static final String RADIUS_SM = "4px";
        public static final String RADIUS_MD = "8px";
        public static final String RADIUS_LG = "12px";
        public static final String RADIUS_XL = "16px";
        public static final String RADIUS_FULL = "9999px";

        public static final int WIDTH_LIGHT = 1;
        public static final int WIDTH_DEFAULT = 2;
        public static final int WIDTH_BOLD = 3;
    }

    /**
     * Definiciones de sombras (box-shadow) para elevación de componentes.
     */
    public static class Shadows {
        public static final String NONE = "none";
        public static final String SM = "0 1px 2px 0 rgba(0, 0, 0, 0.05)";
        public static final String MD = "0 4px 6px -1px rgba(0, 0, 0, 0.1)";
        public static final String LG = "0 10px 15px -3px rgba(0, 0, 0, 0.1)";
        public static final String XL = "0 20px 25px -5px rgba(0, 0, 0, 0.1)";
    }

    /**
     * Tiempos y curvas de transición para animaciones.
     */
    public static class Transitions {
        public static final String FAST = "0.15s ease-in-out";
        public static final String BASE = "0.2s ease-in-out";
        public static final String SLOW = "0.3s ease-in-out";
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Genera la cadena de estilos CSS para un botón primario esmeralda.
     * @return String con estilos CSS.
     */
    public static String getPrimaryButtonStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: %s; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s; " +
                        "-fx-border-radius: %s; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(16, 185, 129, 0.2), 4, 0, 0, 2);",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_BASE,
                Typography.WEIGHT_MEDIUM,
                Colors.EMERALD_500,
                Spacing.SM, Spacing.LG, Spacing.SM, Spacing.LG,
                Borders.RADIUS_MD,
                Borders.RADIUS_MD
        );
    }

    /**
     * Genera la cadena de estilos CSS para un botón primario en estado hover.
     * @return String con estilos CSS.
     */
    public static String getPrimaryButtonHoverStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: %s; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s; " +
                        "-fx-border-radius: %s; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(5, 150, 105, 0.3), 6, 0, 0, 2);",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_BASE,
                Typography.WEIGHT_MEDIUM,
                Colors.EMERALD_600,
                Spacing.SM, Spacing.LG, Spacing.SM, Spacing.LG,
                Borders.RADIUS_MD,
                Borders.RADIUS_MD
        );
    }

    /**
     * Genera la cadena de estilos CSS base para campos de texto (TextField).
     * @return String con estilos CSS.
     */
    public static String getTextFieldStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: %d; " +
                        "-fx-border-radius: %s; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s;",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_BASE,
                Colors.FG_PRIMARY,
                Colors.BG_PRIMARY,
                Colors.BORDER_DEFAULT,
                Borders.WIDTH_DEFAULT,
                Borders.RADIUS_MD,
                Spacing.MD, Spacing.LG, Spacing.MD, Spacing.LG,
                Borders.RADIUS_MD
        );
    }

    /**
     * Genera la cadena de estilos CSS para un campo de texto con foco.
     * @return String con estilos CSS.
     */
    public static String getTextFieldFocusStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: %d; " +
                        "-fx-border-radius: %s; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s; " +
                        "-fx-effect: dropshadow(gaussian, rgba(16, 185, 129, 0.1), 4, 0, 0, 1);",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_BASE,
                Colors.FG_PRIMARY,
                Colors.BG_PRIMARY,
                Colors.EMERALD_500,
                Borders.WIDTH_DEFAULT,
                Borders.RADIUS_MD,
                Spacing.MD, Spacing.LG, Spacing.MD, Spacing.LG,
                Borders.RADIUS_MD
        );
    }

    /**
     * Genera la cadena de estilos CSS estándar para etiquetas (Label).
     * @return String con estilos CSS.
     */
    public static String getLabelStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: %s;",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_LG,
                Typography.WEIGHT_MEDIUM,
                Colors.FG_PRIMARY
        );
    }

    /**
     * Genera estilos CSS para contenedores principales blancos con bordes redondeados.
     * @return String con estilos CSS.
     */
    public static String getContainerStyle() {
        return String.format(
                "-fx-background-color: %s; " +
                        "-fx-padding: %d; " +
                        "-fx-border-radius: %s;",
                Colors.BG_PRIMARY,
                Spacing.LG,
                Borders.RADIUS_LG
        );
    }

    /**
     * Genera estilos CSS para contenedores secundarios con fondo grisáceo.
     * @return String con estilos CSS.
     */
    public static String getSecondaryContainerStyle() {
        return String.format(
                "-fx-background-color: %s; " +
                        "-fx-padding: %d; " +
                        "-fx-border-radius: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: %d;",
                Colors.BG_SECONDARY,
                Spacing.LG,
                Borders.RADIUS_LG,
                Colors.BORDER_DEFAULT,
                Borders.WIDTH_LIGHT
        );
    }

    /**
     * Genera estilos CSS para textos de error.
     * @return String con estilos CSS.
     */
    public static String getErrorTextStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-text-fill: %s;",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_SM,
                Colors.ERROR
        );
    }

    /**
     * Genera estilos CSS base para componentes de tabla (TableView).
     * @return String con estilos CSS.
     */
    public static String getTableStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: %d; " +
                        "-fx-padding: %d;",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_BASE,
                Colors.FG_PRIMARY,
                Colors.BG_PRIMARY,
                Colors.BORDER_DEFAULT,
                Borders.WIDTH_LIGHT,
                Spacing.MD
        );
    }

    /**
     * Genera estilos CSS para los encabezados de las columnas de una tabla.
     * @return String con estilos CSS.
     */
    public static String getTableHeaderStyle() {
        return String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: %d; " +
                        "-fx-padding: %d;",
                Typography.FONT_PRIMARY, Typography.FONT_FALLBACK,
                Typography.SIZE_LG,
                Typography.WEIGHT_SEMIBOLD,
                Colors.FG_PRIMARY,
                Colors.BG_SECONDARY,
                Colors.BORDER_DEFAULT,
                Borders.WIDTH_LIGHT,
                Spacing.MD
        );
    }
}