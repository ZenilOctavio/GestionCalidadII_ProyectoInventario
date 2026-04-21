package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.infrastructure.persistence.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteProductsRepository implements ProductsRepository {

    private final Database db;

    public SQLiteProductsRepository(Database db) {
        this.db = db;
    }

    @Override
    public List<Producto> findAll() {
        String sql = "SELECT id, nombre, precio, cantidad, departamento, almacen_id, descripcion, fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar FROM productos";

        try {
            var stmt = db.conn.prepareStatement(sql);
            var res = stmt.executeQuery();
            var list = new ArrayList<Producto>();

            while(res.next()) {
                var producto = mapResultSetToProducto(res);
                list.add(producto);
            }

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }



    @Override
    public boolean createProduct(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad, departamento, almacen_id, descripcion, fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar) VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, producto.nombre());
            stmt.setDouble(2, producto.precio());
            stmt.setInt(3, producto.cantidad());
            stmt.setString(4, producto.departamento());
            stmt.setInt(5, producto.almacenId());
            stmt.setString(6, producto.descripcion());
            stmt.setInt(7, producto.fechaCreacion());
            stmt.setString(8, producto.fechaModificacion());
            stmt.setString(9, producto.ultimoUsuario());
            var res = stmt.executeUpdate();


            return res != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Optional<Producto> findById(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, id);

            var res = stmt.executeQuery();

            if (!res.next()) {
                return Optional.empty();
            }

            var producto = mapResultSetToProducto(res);
            return Optional.of(producto);
        } catch (SQLException e) {
            return Optional.empty();
        }


    }

    @Override
    public boolean updateProduct(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, precio=?, cantidad=?, descripcion=?, departamento=?, almacen_id=?, fecha_hora_ultima_modificacion=? WHERE id = ?";

        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, producto.nombre());
            stmt.setDouble(2, producto.precio());
            stmt.setInt(3, producto.cantidad());
            stmt.setString(4, producto.descripcion());
            stmt.setString(5, producto.departamento());
            stmt.setInt(6, producto.almacenId());
            stmt.setString(7, producto.fechaModificacion());
            stmt.setInt(8, producto.id());
            var res = stmt.executeUpdate();

            return res != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteProduct(Producto producto) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, producto.id());
            var res = stmt.executeUpdate();

            return res != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Producto mapResultSetToProducto(ResultSet res) throws SQLException {
        return new Producto.Builder()
                .setId(res.getInt("id"))
                .setNombre(res.getString("nombre"))
                .setPrecio(res.getDouble("precio"))
                .setCantidad(res.getInt("cantidad"))
                .setDescripcion(res.getString("descripcion"))
                .setDepartamento(res.getString("departamento"))
                .setAlmacenId(res.getInt("almacen_id"))
                .setFechaCreacion(res.getInt("fecha_hora_creacion"))
                .setFechaModificacion(res.getString("fecha_hora_ultima_modificacion"))
                .setUltimoUsuario(res.getString("ultimo_usuario_en_modificar"))
                .build();
    }
}
