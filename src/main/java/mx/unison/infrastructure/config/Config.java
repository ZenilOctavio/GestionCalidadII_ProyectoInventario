package mx.unison.infrastructure.config;
import io.github.cdimascio.dotenv.Dotenv;
import mx.unison.infrastructure.persistence.DatabaseConfig;

public class Config implements DatabaseConfig {
    static Config instance;

    final public String DB_PATH;
    final public String DB_SCHEMA_RESOURCE;

    private Config(){
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        this.DB_PATH = dotenv.get("DB_PATH");
        this.DB_SCHEMA_RESOURCE = dotenv.get("DB_SCHEMA_RESOURCE");
    }

    public static Config getInstance(){
        if (instance == null){
            instance = new Config();
        }
        return instance;
    }


    @Override
    public String getDbPath() { return DB_PATH; }

    @Override
    public String getDbSchema() {
        return DB_SCHEMA_RESOURCE;
    }
}
