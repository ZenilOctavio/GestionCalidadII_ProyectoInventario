package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.infrastructure.persistence.dao.UsuarioDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de UsersRepository utilizando ORMLite para la persistencia.
 */
public class ORMLiteUsuariosRepository implements UsersRepository {
    private final UsuarioDAO usuarioDAO;

    /**
     * Crea una instancia de ORMLiteUsuariosRepository.
     * @param usuarioDAO El DAO para la entidad Usuario.
     */
    public ORMLiteUsuariosRepository(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Usuario> findAll() {
        try {
            return usuarioDAO.findAll();
        } catch (SQLException ex) {
            System.err.println("Error al obtener todos los usuarios: " + ex.getMessage());
            return List.of();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Usuario> findByName(String name) {
        try {
            return usuarioDAO.findByName(name);
        } catch (SQLException ex) {
            System.err.println("Error al buscar usuario: " + ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Usuario usuario) {
        try {
            usuarioDAO.create(usuario);
        } catch (SQLException ex) {
            System.err.println("Error al crear usuario: " + ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Usuario usuario) {
        try {
            usuarioDAO.update(usuario);
        } catch (SQLException ex) {
            System.err.println("Error al actualizar usuario: " + ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Usuario usuario) {
        try {
            usuarioDAO.delete(usuario);
        } catch (SQLException ex) {
            System.err.println("Error al eliminar usuario: " + ex.getMessage());
        }
    }
}