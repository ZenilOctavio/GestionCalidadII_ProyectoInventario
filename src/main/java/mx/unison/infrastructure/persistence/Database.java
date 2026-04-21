package mx.unison.infrastructure.persistence;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Database {
    private static Database instance;
    private static DatabaseConfig config;

    public Connection conn;

    private Connection connect(String db_path) throws SQLException {
        return DriverManager.getConnection(db_path);
    }

    private Database(String db_path) throws SQLException{
        conn = DriverManager.getConnection(db_path);
    }

    public static void setUp(DatabaseConfig conf) throws SQLException {
        config = conf;
        instance = new Database(conf.getDbPath());
        instance.init(config);
    }

    public static Database getInstance() throws NoConfigEstablished {
        if (instance == null) {
            throw new NoConfigEstablished();
        }
        return instance;
    }

    public void init(DatabaseConfig config) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printAllUsers(){
        String sql = "SELECT * FROM usuarios;";

        try {
            ResultSet res = conn.prepareStatement(sql).executeQuery();
            while (res.next()){
                System.out.println(res.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public Usuario authenticate(String nombre, String passwordPlain) {
//        String sql = "SELECT nombre, rol FROM usuarios WHERE nombre=? AND password=?";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, nombre);
//            ps.setString(2, CryptoUtils.md5(passwordPlain));
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                Usuario u = new Usuario(rs.getString("nombre"), rs.getString("nombre"));
//
//                String upd = "UPDATE usuarios SET fecha_hora_ultimo_inicio=? WHERE nombre=?";
//                try (PreparedStatement pu = conn.prepareStatement(upd)) {
//                    pu.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//                    pu.setString(2, nombre);
//                    pu.executeUpdate();
//                }
//                return u;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public List<Almacen> listAlmacenes() {
//        List<Almacen> out = new ArrayList<>();
//        String sql = "SELECT id, nombre, ubicacion, fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar FROM almacenes";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql)) {
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Almacen a = new Almacen(
//                        rs.getInt("id"),
//                        rs.getString("nombre"),
//                        rs.getString("ubicacion"),
//                        rs.getString("fecha_hora_creacion"),
//                        rs.getString("fecha_hora_ultima_modificacion"),
//                        rs.getString("ultimo_usuario_en_modificar")
//                );
//
//                out.add(a);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return out;
//    }
//    public int insertAlmacen(String nombre, String ubicacion, String usuario) {
//        String sql = "INSERT INTO almacenes(nombre, ubicacion, fecha_hora_creacion, ultimo_usuario_en_modificar) VALUES(?,?,?,?)";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, nombre);
//            ps.setString(2, ubicacion);
//            ps.setString(3, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            ps.setString(4, usuario);
//            ps.executeUpdate();
//            ResultSet g = ps.getGeneratedKeys();
//            if (g.next()) return g.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//    public void updateAlmacen(int id, String nombre, String ubicacion, String usuario) {
//        String sql = "UPDATE almacenes SET nombre=?, ubicacion=?, fecha_hora_ultima_modificacion=?, ultimo_usuario_en_modificar=? WHERE id=?";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql)) {
//            ps.setString(1, nombre);
//            ps.setString(2, ubicacion);
//            ps.setString(3, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            ps.setString(4, usuario);
//            ps.setInt(5, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    public void deleteAlmacen(int id) {
//        String sql = "DELETE FROM almacenes WHERE id=?";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    public List<Producto> listProductos() {
//        List<Producto> out = new ArrayList<>();
//        String sql = "SELECT p.id, p.nombre, p.descripcion, p.cantidad, p.precio, p.almacen_id, a.nombre as almacen_nombre, p.fecha_hora_creacion, p.fecha_hora_ultima_modificacion, p.ultimo_usuario_en_modificar FROM productos AS p LEFT JOIN almacenes a ON p.almacen_id = a.id";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql)) {
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                var pb = new Producto.Builder();
//                pb.setId(rs.getInt("id"));
//                pb.setNombre(rs.getString("nombre"));
//                pb.setDescripcion(rs.getString("descripcion"));
//                pb.setCantidad(rs.getInt("cantidad"));
//                pb.setPrecio(rs.getDouble("precio"));
//                pb.setAlmacenId(rs.getInt("almacen_id"));
//                pb.setFechaCreacion(rs.getString("fecha_hora_creacion"));
//                pb.setFechaModificacion(rs.getString("fecha_hora_ultima_modificacion"));
//                pb.setUltimoUsuario(rs.getString("ultimo_usuario_en_modificar"));
//                out.add(pb.build());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return out;
//    }
//    public int insertProducto(Producto prod, String usuario) {
//        String sql = "INSERT INTO productos(nombre, descripcion, cantidad, precio, almacen_id, fecha_hora_creacion, ultimo_usuario_en_modificar) VALUES(?,?,?,?,?,?,?)";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, prod.nombre);
//            ps.setString(2, prod.descripcion);
//            ps.setInt(3, prod.cantidad);
//            ps.setDouble(4, prod.precio);
//            if (prod.almacenId > 0) ps.setInt(5, prod.almacenId);
//            else ps.setNull(5, Types.INTEGER);
//            ps.setString(6, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            ps.setString(7, usuario);
//            ps.executeUpdate();
//            ResultSet g = ps.getGeneratedKeys();
//            if (g.next()) return g.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//    public void updateProducto(Producto prod, String usuario) {
//        String sql = "UPDATE productos SET nombre=?, descripcion=?, cantidad=?, precio=?, almacen_id=?, fecha_hora_ultima_modificacion=?, ultimo_usuario_en_modificar=? WHERE id=?";
//        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql)) {
//            ps.setString(1, prod.nombre);
//            ps.setString(2, prod.descripcion);
//            ps.setInt(3, prod.cantidad);
//            ps.setDouble(4, prod.precio);
//            if (prod.almacenId > 0) ps.setInt(5, prod.almacenId);
//            else ps.setNull(5, Types.INTEGER);
//            ps.setString(6, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            ps.setString(7, usuario);
//            ps.setInt(8, prod.id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public void deleteProducto(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection c = connect(config.getDbPath()); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


