package mx.unison.presentation.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;

/**
 * Fábrica de componentes visuales reutilizables para la interfaz de usuario.
 * Proporciona métodos estáticos para crear elementos de JavaFX (botones, campos de texto, contenedores)
 * con los estilos predefinidos en {@link ThemeConfig}, asegurando consistencia visual en toda la aplicación.
 */
public class ComponentFactory {

    /**
     * Crea un botón primario con el estilo Emerald definido en el tema.
     * Incluye efectos de transición al pasar el mouse (hover).
     * 
     * @param text El texto que se mostrará en el botón.
     * @return Un objeto {@link Button} configurado y estilizado.
     */
    public static Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.setStyle(ThemeConfig.getPrimaryButtonStyle());
        button.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_BASE));
        button.setPrefHeight(40);
        button.setPrefWidth(120);

        button.setOnMouseEntered(e -> button.setStyle(ThemeConfig.getPrimaryButtonHoverStyle()));
        button.setOnMouseExited(e -> button.setStyle(ThemeConfig.getPrimaryButtonStyle()));

        return button;
    }

    /**
     * Crea un botón secundario con estilo de contorno (outline).
     * Utilizado para acciones menos prominentes o de cancelación.
     * 
     * @param text El texto que se mostrará en el botón.
     * @return Un objeto {@link Button} con estilo secundario.
     */
    public static Button createSecondaryButton(String text) {
        Button button = new Button(text);
        String style = String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: %d; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s; " +
                        "-fx-border-radius: %s; " +
                        "-fx-cursor: hand;",
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_BASE,
                ThemeConfig.Typography.WEIGHT_MEDIUM,
                ThemeConfig.Colors.EMERALD_400,
                ThemeConfig.Colors.BG_SECONDARY,
                ThemeConfig.Colors.BORDER_DEFAULT,
                ThemeConfig.Borders.WIDTH_DEFAULT,
                ThemeConfig.Spacing.SM, ThemeConfig.Spacing.LG, ThemeConfig.Spacing.SM, ThemeConfig.Spacing.LG,
                ThemeConfig.Borders.RADIUS_MD,
                ThemeConfig.Borders.RADIUS_MD
        );
        button.setStyle(style);
        button.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_BASE));
        button.setPrefHeight(40);
        button.setPrefWidth(120);

        return button;
    }

    /**
     * Crea un botón de peligro, típicamente utilizado para acciones de eliminación.
     * Aplica un color rojo de advertencia y efectos de sombra.
     * 
     * @param text El texto que se mostrará en el botón.
     * @return Un objeto {@link Button} con estética de error/peligro.
     */
    public static Button createDangerButton(String text) {
        Button button = new Button(text);
        String style = String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s; " +
                        "-fx-border-radius: %s; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(239, 68, 68, 0.3), 6, 0, 0, 2);",
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_BASE,
                ThemeConfig.Typography.WEIGHT_MEDIUM,
                ThemeConfig.Colors.BG_PRIMARY,
                ThemeConfig.Colors.ERROR,
                ThemeConfig.Spacing.SM, ThemeConfig.Spacing.LG, ThemeConfig.Spacing.SM, ThemeConfig.Spacing.LG,
                ThemeConfig.Borders.RADIUS_MD,
                ThemeConfig.Borders.RADIUS_MD
        );
        button.setStyle(style);
        button.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_BASE));
        button.setPrefHeight(40);
        button.setPrefWidth(120);

        return button;
    }

    /**
     * Crea un campo de texto (TextField) con el diseño del tema.
     * Incluye soporte para texto de sugerencia (prompt) y efectos visuales al obtener el foco.
     * 
     * @param promptText El texto indicativo que aparece cuando el campo está vacío.
     * @return Un objeto {@link TextField} configurado.
     */
    public static TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);

        String style = getTextFieldStyle() +
                " -fx-prompt-text-fill: " + ThemeConfig.Colors.FG_MUTED + ";";

        textField.setStyle(style);
        textField.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_BASE));
        textField.setPrefHeight(40);

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                String focusStyle = getTextFieldFocusStyle() +
                        " -fx-prompt-text-fill: " + ThemeConfig.Colors.FG_MUTED + ";";
                textField.setStyle(focusStyle);
            } else {
                String normalStyle = getTextFieldStyle() +
                        " -fx-prompt-text-fill: " + ThemeConfig.Colors.FG_MUTED + ";";
                textField.setStyle(normalStyle);
            }
        });

        return textField;
    }

    /**
     * Crea un campo de contraseña (PasswordField) con el diseño del tema.
     * Similar a {@link #createStyledTextField(String)} pero oculta los caracteres ingresados.
     * 
     * @param promptText El texto indicativo.
     * @return Un objeto {@link PasswordField} estilizado.
     */
    public static PasswordField createStyledPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);

        String style = getTextFieldStyle() +
                " -fx-prompt-text-fill: " + ThemeConfig.Colors.FG_MUTED + ";";

        passwordField.setStyle(style);
        passwordField.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_BASE));
        passwordField.setPrefHeight(40);

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                String focusStyle = getTextFieldFocusStyle() +
                        " -fx-prompt-text-fill: " + ThemeConfig.Colors.FG_MUTED + ";";
                passwordField.setStyle(focusStyle);
            } else {
                String normalStyle = getTextFieldStyle() +
                        " -fx-prompt-text-fill: " + ThemeConfig.Colors.FG_MUTED + ";";
                passwordField.setStyle(normalStyle);
            }
        });

        return passwordField;
    }

    /**
     * Crea un selector desplegable (ComboBox) con los estilos base del tema.
     * 
     * @param <T> El tipo de datos que contendrá el ComboBox.
     * @return Un objeto {@link ComboBox} configurado.
     */
    public static <T> ComboBox<T> createStyledComboBox() {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setStyle(getTextFieldStyle());
        comboBox.setPrefHeight(40);

        return comboBox;
    }

    /**
     * Crea una etiqueta (Label) con el estilo estándar de la aplicación.
     * 
     * @param text El contenido de la etiqueta.
     * @return Un objeto {@link Label} estilizado.
     */
    public static Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle(ThemeConfig.getLabelStyle());
        label.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_LG));
        label.setTextFill(Color.web(ThemeConfig.Colors.FG_PRIMARY));

        return label;
    }

    /**
     * Crea una etiqueta para títulos de gran tamaño (Hero section).
     * 
     * @param text El texto del título.
     * @return Un objeto {@link Label} con tipografía negrita y tamaño grande.
     */
    public static Label createTitleLabel(String text) {
        Label label = new Label(text);
        String style = String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: %s;",
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_4XL,
                ThemeConfig.Typography.WEIGHT_BOLD,
                ThemeConfig.Colors.FG_PRIMARY
        );
        label.setStyle(style);
        label.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_4XL));

        return label;
    }

    /**
     * Crea una etiqueta para subtítulos.
     * 
     * @param text El texto del subtítulo.
     * @return Un objeto {@link Label} con estilo secundario.
     */
    public static Label createSubtitleLabel(String text) {
        Label label = new Label(text);
        String style = String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: %s;",
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_LG,
                ThemeConfig.Typography.WEIGHT_NORMAL,
                ThemeConfig.Colors.FG_SECONDARY
        );
        label.setStyle(style);
        label.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_LG));

        return label;
    }

    /**
     * Crea una etiqueta para mensajes de error.
     * 
     * @param text El mensaje de error.
     * @return Un objeto {@link Label} en color rojo.
     */
    public static Label createErrorLabel(String text) {
        Label label = new Label(text);
        label.setStyle(ThemeConfig.getErrorTextStyle());
        label.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_SM));
        label.setTextFill(Color.web(ThemeConfig.Colors.ERROR));
        label.setWrapText(true);

        return label;
    }

    /**
     * Crea una etiqueta pequeña para información auxiliar o pies de página.
     * 
     * @param text El texto informativo.
     * @return Un objeto {@link Label} de tamaño reducido y color atenuado.
     */
    public static Label createSmallLabel(String text) {
        Label label = new Label(text);
        String style = String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-text-fill: %s;",
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_SM,
                ThemeConfig.Colors.FG_SECONDARY
        );
        label.setStyle(style);
        label.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_SM));

        return label;
    }

    /**
     * Crea un contenedor vertical (VBox) configurado para formularios.
     * 
     * @return Un objeto {@link VBox} con espaciado y márgenes estándar.
     */
    public static VBox createFormContainer() {
        VBox container = new VBox();
        container.setSpacing(ThemeConfig.Spacing.LG);
        container.setPadding(new Insets(ThemeConfig.Spacing.XL));
        container.setStyle(ThemeConfig.getContainerStyle());

        return container;
    }

    /**
     * Crea un contenedor vertical con fondo secundario.
     * 
     * @return Un objeto {@link VBox} con estilo de tarjeta secundaria.
     */
    public static VBox createSecondaryContainer() {
        VBox container = new VBox();
        container.setSpacing(ThemeConfig.Spacing.MD);
        container.setPadding(new Insets(ThemeConfig.Spacing.LG));
        container.setStyle(ThemeConfig.getSecondaryContainerStyle());

        return container;
    }

    /**
     * Agrupa una etiqueta con su respectivo campo de texto.
     * 
     * @param labelText Texto descriptivo del campo.
     * @param textField Componente de entrada de texto.
     * @return Un contenedor {@link VBox} con el grupo formado.
     */
    public static VBox createFormFieldGroup(String labelText, TextField textField) {
        VBox group = new VBox();
        group.setSpacing(ThemeConfig.Spacing.SM);

        Label label = createStyledLabel(labelText);
        group.getChildren().addAll(label, textField);

        return group;
    }

    /**
     * Agrupa una etiqueta con un campo de contraseña.
     * 
     * @param labelText Texto descriptivo.
     * @param passwordField Componente de entrada de contraseña.
     * @return Un contenedor {@link VBox}.
     */
    public static VBox createFormFieldGroup(String labelText, PasswordField passwordField) {
        VBox group = new VBox();
        group.setSpacing(ThemeConfig.Spacing.SM);

        Label label = createStyledLabel(labelText);
        group.getChildren().addAll(label, passwordField);

        return group;
    }

    /**
     * Agrupa una etiqueta con un ComboBox.
     * 
     * @param labelText Texto descriptivo.
     * @param comboBox Selector de opciones.
     * @param <T> Tipo de datos del ComboBox.
     * @return Un contenedor {@link VBox}.
     */
    public static <T> VBox createFormFieldGroup(String labelText, ComboBox<T> comboBox) {
        VBox group = new VBox();
        group.setSpacing(ThemeConfig.Spacing.SM);

        Label label = createStyledLabel(labelText);
        group.getChildren().addAll(label, comboBox);

        return group;
    }

    /**
     * Crea un contenedor horizontal para alinear botones.
     * 
     * @param buttons Lista de botones a incluir.
     * @return Un objeto {@link HBox} con espaciado uniforme.
     */
    public static HBox createButtonBox(Button... buttons) {
        HBox box = new HBox();
        box.setSpacing(ThemeConfig.Spacing.MD);
        box.getChildren().addAll(buttons);

        return box;
    }

    /**
     * Crea una barra de herramientas (Toolbar) con título y botones de acción.
     * 
     * @param title Título de la sección o vista.
     * @param buttons Botones de acción rápida.
     * @return Un {@link HBox} configurado como cabecera o barra de herramientas.
     */
    public static HBox createToolbar(String title, Button... buttons) {
        HBox toolbar = new HBox();
        toolbar.setSpacing(ThemeConfig.Spacing.MD);
        toolbar.setPadding(new Insets(ThemeConfig.Spacing.LG));
        toolbar.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: 0 0 1 0;",
                ThemeConfig.Colors.BG_SECONDARY,
                ThemeConfig.Colors.BORDER_DEFAULT
        ));

        Label titleLabel = createStyledLabel(title);
        toolbar.getChildren().add(titleLabel);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        toolbar.getChildren().add(spacer);

        for (Button button : buttons) {
            toolbar.getChildren().add(button);
        }

        return toolbar;
    }

    /**
     * Obtiene el estilo CSS base para los campos de texto del sistema.
     * 
     * @return Cadena con propiedades CSS de JavaFX.
     */
    private static String getTextFieldStyle() {
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
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_BASE,
                ThemeConfig.Colors.FG_PRIMARY,
                ThemeConfig.Colors.BG_PRIMARY,
                ThemeConfig.Colors.BORDER_DEFAULT,
                ThemeConfig.Borders.WIDTH_DEFAULT,
                ThemeConfig.Borders.RADIUS_MD,
                ThemeConfig.Spacing.MD, ThemeConfig.Spacing.LG, ThemeConfig.Spacing.MD, ThemeConfig.Spacing.LG,
                ThemeConfig.Borders.RADIUS_MD
        );
    }

    /**
     * Obtiene el estilo CSS para campos de texto cuando tienen el foco.
     * 
     * @return Cadena con propiedades CSS de JavaFX incluyendo efectos de brillo.
     */
    private static String getTextFieldFocusStyle() {
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
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_BASE,
                ThemeConfig.Colors.FG_PRIMARY,
                ThemeConfig.Colors.BG_PRIMARY,
                ThemeConfig.Colors.EMERALD_500,
                ThemeConfig.Borders.WIDTH_DEFAULT,
                ThemeConfig.Borders.RADIUS_MD,
                ThemeConfig.Spacing.MD, ThemeConfig.Spacing.LG, ThemeConfig.Spacing.MD, ThemeConfig.Spacing.LG,
                ThemeConfig.Borders.RADIUS_MD
        );
    }
}