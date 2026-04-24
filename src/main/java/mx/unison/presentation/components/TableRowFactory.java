package mx.unison.presentation.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mx.unison.presentation.theme.FontLoader;
import mx.unison.presentation.theme.ThemeConfig;

/**
 * Fábrica especializada en la creación de componentes para tablas (TableView).
 * Permite generar columnas y filas personalizadas siguiendo el estilo visual moderno
 * establecido en la variante Lyra del proyecto.
 */
public class TableRowFactory {

    /**
     * Crea una columna de tabla pre-estilizada.
     * 
     * @param <T> El tipo de objeto que representa la fila de la tabla.
     * @param headerText El texto que se mostrará en el encabezado de la columna.
     * @param prefWidth El ancho preferido de la columna.
     * @return Una {@link TableColumn} configurada con los estilos de cabecera del tema.
     */
    public static <T> TableColumn<T, String> createTableColumn(String headerText, double prefWidth) {
        TableColumn<T, String> column = new TableColumn<>(headerText);
        column.setPrefWidth(prefWidth);
        column.setStyle(ThemeConfig.getTableHeaderStyle());
        column.setReorderable(false);
        column.setSortable(true);

        return column;
    }

    /**
     * Crea un contenedor horizontal para agrupar botones de acción dentro de una celda de tabla.
     * 
     * @param buttons Los botones (ej. Editar, Eliminar) que se incluirán en la celda.
     * @return Un {@link HBox} con espaciado y alineación adecuados para la tabla.
     */
    public static HBox createActionButtonsContainer(Button... buttons) {
        HBox container = new HBox();
        container.setSpacing(ThemeConfig.Spacing.SM);
        container.setPadding(new Insets(ThemeConfig.Spacing.XS));
        container.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-border-radius: %s;",
                ThemeConfig.Colors.BG_SECONDARY,
                ThemeConfig.Borders.RADIUS_SM
        ));

        for (Button button : buttons) {
            button.setPrefHeight(28);
            button.setPrefWidth(60);
            button.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_SM));
        }

        container.getChildren().addAll(buttons);
        return container;
    }

    /**
     * Crea un botón de acción de tamaño reducido para ser utilizado dentro de filas de tablas.
     * 
     * @param text El texto del botón.
     * @param color El color de fondo en formato hexadecimal.
     * @return Un {@link Button} compacto y estilizado.
     */
    public static Button createSmallActionButton(String text, String color) {
        Button button = new Button(text);
        String style = String.format(
                "-fx-font-family: '%s', '%s'; " +
                        "-fx-font-size: %dpx; " +
                        "-fx-font-weight: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: %s; " +
                        "-fx-padding: %d %d %d %d; " +
                        "-fx-background-radius: %s; " +
                        "-fx-border-radius: %s; " +
                        "-fx-cursor: hand;",
                ThemeConfig.Typography.FONT_PRIMARY, ThemeConfig.Typography.FONT_FALLBACK,
                ThemeConfig.Typography.SIZE_SM,
                ThemeConfig.Typography.WEIGHT_MEDIUM,
                color,
                ThemeConfig.Spacing.XS, ThemeConfig.Spacing.MD, ThemeConfig.Spacing.XS, ThemeConfig.Spacing.MD,
                ThemeConfig.Borders.RADIUS_SM,
                ThemeConfig.Borders.RADIUS_SM
        );
        button.setStyle(style);
        button.setFont(FontLoader.getFont(ThemeConfig.Typography.SIZE_SM));

        return button;
    }

    /**
     * Crea un encabezado visual para la sección de tabla.
     * 
     * @param title El título descriptivo de la tabla.
     * @return Un {@link VBox} con el título estilizado y separador inferior.
     */
    public static VBox createTableHeader(String title) {
        VBox header = new VBox();
        header.setSpacing(ThemeConfig.Spacing.MD);
        header.setPadding(new Insets(ThemeConfig.Spacing.LG));
        header.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: 0 0 1 0;",
                ThemeConfig.Colors.BG_SECONDARY,
                ThemeConfig.Colors.BORDER_DEFAULT
        ));

        Label titleLabel = ComponentFactory.createTitleLabel(title);
        header.getChildren().add(titleLabel);

        return header;
    }
}