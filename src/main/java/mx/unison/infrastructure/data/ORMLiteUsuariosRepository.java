package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.infrastructure.persistence.dao.UsuarioDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ORMLiteUsuariosRepository implements UsersRepository {
    private final UsuarioDAO usuarioDAO;

    public ORMLiteUsuariosRepository(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public List<Usuario> findAll() {
        try {
            return usuarioDAO.findAll();
        } catch (SQLException ex) {
            System.err.println("Error al obtener todos los usuarios: " + ex.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<Usuario> findByName(String name) {
        try {
            return usuarioDAO.findByName(name);
        } catch (SQLException ex) {
            System.err.println("Error al buscar usuario: " + ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void create(Usuario usuario) {
        try {
            usuarioDAO.create(usuario);
        } catch (SQLException ex) {
            System.err.println("Error al crear usuario: " + ex.getMessage());
        }
    }

    @Override
    public void update(Usuario usuario) {
        try {
            usuarioDAO.update(usuario);
        } catch (SQLException ex) {
            System.err.println("Error al actualizar usuario: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Usuario usuario) {
        try {
            usuarioDAO.delete(usuario);
        } catch (SQLException ex) {
            System.err.println("Error al eliminar usuario: " + ex.getMessage());
        }
    }
}