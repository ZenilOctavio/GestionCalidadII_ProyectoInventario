package mx.unison.infrastructure.persistence.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.core.domain.models.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoDAO {
    private final Dao<Producto, Integer> dao;

    public ProductoDAO(Dao<Producto, Integer> dao) {
        this.dao = dao;
    }

    public List<Producto> findAll() throws SQLException {
        return dao.queryForAll();
    }

    public Optional<Producto> findById(int id) throws SQLException {
        Producto producto = dao.queryForId(id);
        return Optional.ofNullable(producto);
    }

    public void create(Producto producto) throws SQLException {
        dao.create(producto);
    }

    public void update(Producto producto) throws SQLException {
        dao.update(producto);
    }

    public void delete(Producto producto) throws SQLException {
        dao.delete(producto);
    }

    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}