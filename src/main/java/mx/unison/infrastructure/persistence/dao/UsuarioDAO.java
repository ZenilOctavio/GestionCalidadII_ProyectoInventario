package mx.unison.infrastructure.persistence.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import mx.unison.core.domain.models.Usuario;

import java.sql.SQLException;
import java.util.Optional;

public class UsuarioDAO {
    private final Dao<Usuario, String> dao;

    public UsuarioDAO(Dao<Usuario, String> dao) {
        this.dao = dao;
    }

    public Optional<Usuario> findByName(String name) throws SQLException {
        Usuario usuario = dao.queryBuilder()
                .where()
                .eq("nombre", name)
                .queryForFirst();
        return Optional.ofNullable(usuario);
    }

    public void create(Usuario usuario) throws SQLException {
        dao.create(usuario);
    }

    public void update(Usuario usuario) throws SQLException {
        dao.update(usuario);
    }

    public void delete(Usuario usuario) throws SQLException {
        dao.delete(usuario);
    }

    public Optional<Usuario> findById(String id) throws SQLException {
        Usuario usuario = dao.queryForId(id);
        return Optional.ofNullable(usuario);
    }
}