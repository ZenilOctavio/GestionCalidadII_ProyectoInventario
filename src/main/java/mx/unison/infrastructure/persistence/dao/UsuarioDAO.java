package mx.unison.infrastructure.persistence.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.core.domain.models.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Usuario utilizando ORMLite.
 */
public class UsuarioDAO {
    private final Dao<Usuario, String> dao;

    /**
     * Crea una instancia de UsuarioDAO.
     * @param dao El objeto DAO de ORMLite.
     */
    public UsuarioDAO(Dao<Usuario, String> dao) {
        this.dao = dao;
    }

    /**
     * Busca un usuario por su nombre.
     *
     * @param name El nombre de usuario.
     * @return Un Optional con el usuario.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public Optional<Usuario> findByName(String name) throws SQLException {
        Usuario usuario = dao.queryBuilder()
                .where()
                .eq("nombre", name)
                .queryForFirst();
        return Optional.ofNullable(usuario);
    }

    /**
     * Recupera todos los usuarios.
     * @return Una lista de usuarios.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public List<Usuario> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Crea un nuevo registro de usuario.
     * @param usuario El usuario a crear.
     * @throws SQLException Si ocurre un error en la inserción.
     */
    public void create(Usuario usuario) throws SQLException {
        dao.create(usuario);
    }

    /**
     * Actualiza un registro de usuario.
     * @param usuario El usuario con los cambios.
     * @throws SQLException Si ocurre un error en la actualización.
     */
    public void update(Usuario usuario) throws SQLException {
        dao.update(usuario);
    }

    /**
     * Elimina un registro de usuario.
     * @param usuario El usuario a eliminar.
     * @throws SQLException Si ocurre un error en la eliminación.
     */
    public void delete(Usuario usuario) throws SQLException {
        dao.delete(usuario);
    }

    /**
     * Busca un usuario por su ID (nombre).
     *
     * @param id El nombre de usuario.
     * @return Un Optional con el usuario.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public Optional<Usuario> findById(String id) throws SQLException {
        Usuario usuario = dao.queryForId(id);
        return Optional.ofNullable(usuario);
    }
}