package mx.unison.infrastructure.config;
import io.github.cdimascio.dotenv.Dotenv;
import mx.unison.infrastructure.persistence.DatabaseConfig;

/**
 * Implementación de la configuración del sistema cargada desde variables de entorno.
 * Utiliza el patrón Singleton para asegurar una única instancia de la configuración.
 */
public class Config implements DatabaseConfig {
    static Config instance;

    /** Ruta al archivo de base de datos. */
    final public String DB_PATH;
    /** Ruta al recurso del esquema SQL. */
    final public String DB_SCHEMA_RESOURCE;

    private Config(){
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        this.DB_PATH = dotenv.get("DB_PATH");
        this.DB_SCHEMA_RESOURCE = dotenv.get("DB_SCHEMA_RESOURCE");
    }

    /**
     * Obtiene la instancia única de la configuración.
     * @return La instancia de Config.
     */
    public static Config getInstance(){
        if (instance == null){
            instance = new Config();
        }
        return instance;
    }


    /**
     * Obtiene la ruta de la base de datos configurada.
     * @return La ruta del archivo de base de datos.
     */
    @Override
    public String getDbPath() { return DB_PATH; }

    /**
     * Obtiene la ruta del esquema de la base de datos.
     * @return La ruta del recurso SQL.
     */
    @Override
    public String getDbSchema() {
        return DB_SCHEMA_RESOURCE;
    }
}
