package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.infrastructure.persistence.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de AlmacenesRepository utilizando JDBC directo para SQLite.
 */
public class SQLiteAlmacenesRepository implements AlmacenesRepository {
    private final Database db;

    /**
     * Crea una instancia de SQLiteAlmacenesRepository.
     * @param db La conexión a la base de datos.
     */
    public SQLiteAlmacenesRepository(Database db) {
        this.db = db;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Almacen> findAll() {
        String sql = "SELECT id, nombre, fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar FROM almacenes";

        try {
            var stmt = db.conn.prepareStatement(sql);
            var res = stmt.executeQuery();
            var list = new ArrayList<Almacen>();

            while(res.next()) {
                var almacen = mapResultSetToAlmacen(res);
                list.add(almacen);
            }

            return list;

        } catch (SQLException e) {
            return List.of();
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createAlmacen(Almacen almacen) {
        String sql = "INSERT INTO almacenes (nombre, fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar) VALUES (?,?,?,?)";

        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, almacen.nombre());
            stmt.setString(2, almacen.fechaHoraCreacion());
            stmt.setString(3, almacen.fechaHoraUltimaMod());
            stmt.setString(4, almacen.ultimoUsuario());
            var res = stmt.executeUpdate();

            return res != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Almacen> findById(int id) {
        String sql = "SELECT * FROM almacenes WHERE id = ?";

        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, id);


            var res = stmt.executeQuery();

            if (!res.next()) {
                return Optional.empty();
            }

            var producto = mapResultSetToAlmacen(res);
            return Optional.of(producto);
        } catch (SQLException e) {
            return Optional.empty();
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateAlmacen(Almacen almacen) {
        String sql = "UPDATE almacenes SET nombre=?, fecha_hora_ultima_modificacion=?, ultimo_usuario_en_modificar=? WHERE id = ?";

        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, almacen.nombre());
            stmt.setString(2, almacen.fechaHoraUltimaMod());
            stmt.setString(3, almacen.ultimoUsuario());
            stmt.setInt(4, almacen.id());
            var res = stmt.executeUpdate();


            return res != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteAlmacen(Almacen almacen) {
        String sql = "DELETE FROM almacenes WHERE id = ?";
        try {
            var stmt = db.conn.prepareStatement(sql);
            stmt.setInt(1, almacen.id());
            var res = stmt.executeUpdate();

            return res != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Almacen.
     *
     * @param res El ResultSet de la consulta.
     * @return Un objeto Almacen poblado.
     * @throws SQLException Si ocurre un error al acceder a los datos.
     */
    private Almacen mapResultSetToAlmacen(ResultSet res) throws SQLException {
        var almacen = new Almacen();
        almacen.setId(res.getInt("id"));
        almacen.setNombre(res.getString("nombre"));
        almacen.setFechaHoraCreacion(res.getString("fecha_hora_creacion"));
        almacen.setFechaHoraUltimaMod(res.getString("fecha_hora_ultima_modificacion"));
        almacen.setUltimoUsuario(res.getString("ultimo_usuario_en_modificar"));
        return almacen;
    }

}
