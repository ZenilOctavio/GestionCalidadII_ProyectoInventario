package mx.unison.infrastructure.persistence;

/**
 * Interfaz que define la configuración requerida para la base de datos.
 */
public interface DatabaseConfig {
    /**
     * Obtiene la ruta al archivo de base de datos.
     * @return La ruta como String.
     */
    String getDbPath();

    /**
     * Obtiene la ruta del recurso que contiene el esquema SQL inicial.
     * @return La ruta del recurso.
     */
    String getDbSchema();
}
