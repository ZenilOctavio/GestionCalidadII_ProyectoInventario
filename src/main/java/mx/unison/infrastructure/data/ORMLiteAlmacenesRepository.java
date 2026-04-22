package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.infrastructure.persistence.dao.AlmacenDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ORMLiteAlmacenesRepository implements AlmacenesRepository {
    private final AlmacenDAO almacenDAO;

    public ORMLiteAlmacenesRepository(AlmacenDAO almacenDAO) {
        this.almacenDAO = almacenDAO;
    }

    @Override
    public List<Almacen> findAll() {
        try {
            return almacenDAO.findAll();
        } catch (SQLException ex) {
            System.err.println("Error al obtener todos los almacenes: " + ex.getMessage());
            ex.printStackTrace();
            return List.of();
        }
    }

    @Override
    public boolean createAlmacen(Almacen almacen) {
        try {
            almacenDAO.create(almacen);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al crear almacén: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Almacen> findById(int id) {
        try {
            return almacenDAO.findById(id);
        } catch (SQLException ex) {
            System.err.println("Error al buscar almacén por id: " + ex.getMessage());
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean updateAlmacen(Almacen almacen) {
        try {
            almacenDAO.update(almacen);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al actualizar almacén: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAlmacen(Almacen almacen) {
        try {
            almacenDAO.delete(almacen);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar almacén: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}