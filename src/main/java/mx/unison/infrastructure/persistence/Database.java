package mx.unison.infrastructure.persistence;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.models.Usuario;
import mx.unison.infrastructure.persistence.dao.AlmacenDAO;
import mx.unison.infrastructure.persistence.dao.ProductoDAO;
import mx.unison.infrastructure.persistence.dao.UsuarioDAO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

/**
 * Clase central para la gestión de la base de datos.
 * Maneja tanto la conexión JDBC directa (legacy) como la integración con ORMLite.
 */
public class Database {
    private static Database instance;
    private static DatabaseConfig config;

    /** Conexión JDBC directa para compatibilidad con repositorios legacy. */
    public Connection conn;

    // ORMLite (para nuevos DAOs)
    private ConnectionSource connectionSource;
    private UsuarioDAO usuarioDAO;
    private ProductoDAO productoDAO;
    private AlmacenDAO almacenDAO;

    private Connection connect(String db_path) throws SQLException {
        return DriverManager.getConnection(db_path);
    }

    private Database(String db_path) throws SQLException {
        this.conn = DriverManager.getConnection(db_path);
    }

    /**
     * Configura e inicializa la instancia única de la base de datos.
     *
     * @param conf La configuración de la base de datos.
     * @throws SQLException Si ocurre un error durante la conexión.
     */
    public static void setUp(DatabaseConfig conf) throws SQLException {
        config = conf;
        instance = new Database(conf.getDbPath());
        instance.initLegacy(config);      // Inicializa el esquema legacy
        instance.initORMLite(conf.getDbPath()); // Inicializa ORMLite DAOs
    }

    /**
     * Obtiene la instancia actual de la base de datos.
     *
     * @return La instancia de Database.
     * @throws NoConfigEstablished Si la base de datos no ha sido inicializada con setUp.
     */
    public static Database getInstance() throws NoConfigEstablished {
        if (instance == null) {
            throw new NoConfigEstablished();
        }
        return instance;
    }

    /**
     * Inicialización LEGACY: Carga el schema.sql desde recursos.
     * Mantiene compatibilidad con repositorios SQLite directos.
     *
     * @param config Configuración de la base de datos.
     */
    private void initLegacy(DatabaseConfig config) {
        InputStream schema;

        if (config.getDbSchema().trim().isEmpty()) {
            throw new IllegalArgumentException("Config Class has an empty DB_SCHEMA_RESOURCE variable");
        }

        schema = getClass().getClassLoader().getResourceAsStream(config.getDbSchema());

        if (schema == null) {
            throw new RuntimeException("Couldn't read the database schema \"%s\"".formatted(config.getDbSchema()));
        }

        try (Connection c = connect(config.getDbPath()); Statement st = c.createStatement()) {
            String schemaSql;

            BufferedReader reader = new BufferedReader(new InputStreamReader(schema));
            schemaSql = reader.lines().collect(Collectors.joining("\n"));

            String[] queries = schemaSql.split(";");
            for (String query : queries) {
                String trimmedQuery = query.trim();
                if (!trimmedQuery.isEmpty()) {
                    st.execute(trimmedQuery);
                }
            }
            System.out.println("✓ Schema legacy cargado correctamente");

        } catch (SQLException e) {
            System.err.println("⚠ Error al cargar schema legacy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inicialización ORMLITE: Crea DAOs y configura ORMLite.
     *
     * @param dbPath Ruta a la base de datos.
     */
    private void initORMLite(String dbPath) {
        try {
            this.connectionSource = new JdbcConnectionSource(dbPath);

            // Crear tablas si no existen (compatible con schema legacy)
            TableUtils.createTableIfNotExists(connectionSource, Usuario.class);
            TableUtils.createTableIfNotExists(connectionSource, Producto.class);
            TableUtils.createTableIfNotExists(connectionSource, Almacen.class);

            // Inicializar DAOs
            Dao<Usuario, String> usuarioDaoORM = DaoManager.createDao(connectionSource, Usuario.class);
            Dao<Producto, Integer> productoDaoORM = DaoManager.createDao(connectionSource, Producto.class);
            Dao<Almacen, Integer> almacenDaoORM = DaoManager.createDao(connectionSource, Almacen.class);

            this.usuarioDAO = new UsuarioDAO(usuarioDaoORM);
            this.productoDAO = new ProductoDAO(productoDaoORM);
            this.almacenDAO = new AlmacenDAO(almacenDaoORM);

            System.out.println("✓ ORMLite inicializado correctamente");

        } catch (SQLException e) {
            System.err.println("⚠ Error al inicializar ORMLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== GETTERS LEGACY (para SQLiteRepository originales) =====
    /**
     * Obtiene la conexión JDBC directa.
     * @return La conexión Connection.
     */
    public Connection getConnection() {
        return conn;
    }

    // ===== GETTERS ORMLITE (para nuevos DAO Repositories) =====
    /**
     * Obtiene el DAO de usuarios.
     * @return El objeto UsuarioDAO.
     */
    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    /**
     * Obtiene el DAO de productos.
     * @return El objeto ProductoDAO.
     */
    public ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    /**
     * Obtiene el DAO de almacenes.
     * @return El objeto AlmacenDAO.
     */
    public AlmacenDAO getAlmacenDAO() {
        return almacenDAO;
    }

    /**
     * Obtiene la fuente de conexión de ORMLite.
     * @return El objeto ConnectionSource.
     */
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Cierra todas las conexiones activas a la base de datos.
     */
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            if (connectionSource != null) {
                connectionSource.close();
            }
            System.out.println("✓ Conexiones cerradas correctamente");
        } catch (Exception e) {
            System.err.println("✗ Error al cerrar conexiones: " + e.getMessage());
        }

    }

    /**
     * Método legacy para depuración que imprime IDs de usuarios.
     */
    public void printAllUsers() {
        String sql = "SELECT * FROM usuarios;";
        try {
            ResultSet res = conn.prepareStatement(sql).executeQuery();
            while (res.next()) {
                System.out.println(res.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}