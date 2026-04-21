package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Almacen;

import java.util.List;
import java.util.Optional;

public interface AlmacenesRepository {
    List<Almacen> findAll();
    boolean createAlmacen(Almacen almacen);
    Optional<Almacen> findById(int id);
    boolean updateAlmacen(Almacen almacen);
    boolean deleteAlmacen(Almacen almacen);
}
