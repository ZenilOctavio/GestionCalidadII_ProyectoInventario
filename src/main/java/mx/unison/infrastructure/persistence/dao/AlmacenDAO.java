package mx.unison.infrastructure.persistence.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.core.domain.models.Almacen;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Almacen utilizando ORMLite.
 */
public class AlmacenDAO {
    private final Dao<Almacen, Integer> dao;

    /**
     * Crea una instancia de AlmacenDAO.
     * @param dao El objeto DAO de ORMLite.
     */
    public AlmacenDAO(Dao<Almacen, Integer> dao) {
        this.dao = dao;
    }

    /**
     * Recupera todos los almacenes.
     * @return Una lista de almacenes.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public List<Almacen> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Busca un almacén por su ID.
     *
     * @param id El identificador del almacén.
     * @return Un Optional con el almacén.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public Optional<Almacen> findById(int id) throws SQLException {
        Almacen almacen = dao.queryForId(id);
        return Optional.ofNullable(almacen);
    }

    /**
     * Crea un nuevo registro de almacén.
     * @param almacen El almacén a crear.
     * @throws SQLException Si ocurre un error en la inserción.
     */
    public void create(Almacen almacen) throws SQLException {
        dao.create(almacen);
    }

    /**
     * Actualiza un registro de almacén.
     * @param almacen El almacén con los cambios.
     * @throws SQLException Si ocurre un error en la actualización.
     */
    public void update(Almacen almacen) throws SQLException {
        dao.update(almacen);
    }

    /**
     * Elimina un registro de almacén.
     * @param almacen El almacén a eliminar.
     * @throws SQLException Si ocurre un error en la eliminación.
     */
    public void delete(Almacen almacen) throws SQLException {
        dao.delete(almacen);
    }

    /**
     * Elimina un almacén por su ID.
     * @param id El identificador del almacén.
     * @throws SQLException Si ocurre un error en la eliminación.
     */
    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}