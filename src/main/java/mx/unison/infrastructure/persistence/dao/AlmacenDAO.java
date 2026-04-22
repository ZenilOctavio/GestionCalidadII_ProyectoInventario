package mx.unison.infrastructure.persistence.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.core.domain.models.Almacen;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AlmacenDAO {
    private final Dao<Almacen, Integer> dao;

    public AlmacenDAO(Dao<Almacen, Integer> dao) {
        this.dao = dao;
    }

    public List<Almacen> findAll() throws SQLException {
        return dao.queryForAll();
    }

    public Optional<Almacen> findById(int id) throws SQLException {
        Almacen almacen = dao.queryForId(id);
        return Optional.ofNullable(almacen);
    }

    public void create(Almacen almacen) throws SQLException {
        dao.create(almacen);
    }

    public void update(Almacen almacen) throws SQLException {
        dao.update(almacen);
    }

    public void delete(Almacen almacen) throws SQLException {
        dao.delete(almacen);
    }

    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}