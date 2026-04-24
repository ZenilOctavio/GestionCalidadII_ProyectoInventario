package mx.unison.presentation.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase de utilidad para el formateo de datos en la capa de presentación.
 * Proporciona métodos estáticos para estandarizar la visualización de moneda,
 * números, fechas y manipulación básica de cadenas de texto en la interfaz de usuario.
 */
public class FormatterUtil {

    private static final DecimalFormat CURRENCY_FORMAT;
    private static final DecimalFormat NUMBER_FORMAT;
    private static final SimpleDateFormat DATE_FORMAT;
    private static final SimpleDateFormat DATETIME_FORMAT;

    static {
        // Configurar formato de moneda (USD/México style: $,###.##)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setCurrencySymbol("$");
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        CURRENCY_FORMAT = new DecimalFormat("$#,##0.00", symbols);

        // Configurar formato de números con separadores de miles
        NUMBER_FORMAT = new DecimalFormat("#,##0", symbols);

        // Configurar formatos de fecha y hora estándar (DD/MM/YYYY)
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    /**
     * Formatea un valor numérico como moneda.
     * Ejemplo: 1500.5 -> "$1,500.50"
     * 
     * @param amount El monto a formatear.
     * @return Cadena de texto con el formato de moneda.
     */
    public static String formatCurrency(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    /**
     * Formatea un número entero con separadores de miles.
     * Ejemplo: 15000 -> "15,000"
     * 
     * @param number El número entero.
     * @return Cadena de texto formateada.
     */
    public static String formatNumber(int number) {
        return NUMBER_FORMAT.format(number);
    }

    /**
     * Formatea un número decimal con separadores de miles.
     * 
     * @param number El número decimal.
     * @return Cadena de texto formateada.
     */
    public static String formatNumber(double number) {
        return NUMBER_FORMAT.format(number);
    }

    /**
     * Convierte un timestamp (milisegundos) a una fecha legible (dd/MM/yyyy).
     * 
     * @param timestamp El tiempo en milisegundos desde el epoch.
     * @return Fecha formateada como texto.
     */
    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }

    /**
     * Convierte un timestamp (milisegundos) a fecha y hora completa.
     * 
     * @param timestamp El tiempo en milisegundos.
     * @return Fecha y hora formateadas.
     */
    public static String formatDateTime(long timestamp) {
        return DATETIME_FORMAT.format(new Date(timestamp));
    }

    /**
     * Intenta formatear una cadena que representa una fecha o timestamp.
     * Si la cadena es un valor numérico, lo trata como un timestamp.
     * 
     * @param dateString La cadena de entrada a procesar.
     * @return La fecha formateada o la cadena original si no es procesable.
     */
    public static String formatDateString(String dateString) {
        try {
            if (dateString == null || dateString.isEmpty()) return "N/A";
            long timestamp = Long.parseLong(dateString);
            return formatDateTime(timestamp);
        } catch (NumberFormatException e) {
            return dateString;
        }
    }

    /**
     * Recorta una cadena de texto a una longitud máxima, añadiendo puntos suspensivos.
     * 
     * @param text El texto original.
     * @param maxLength La longitud máxima permitida.
     * @return El texto truncado.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Capitaliza la primera letra de una cadena y pone el resto en minúsculas.
     * Ejemplo: "PRODUCTOS" -> "Productos"
     * 
     * @param text El texto a transformar.
     * @return El texto capitalizado.
     */
    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}