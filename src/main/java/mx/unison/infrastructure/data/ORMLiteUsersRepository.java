package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.infrastructure.persistence.dao.UsuarioDAO;

import java.sql.SQLException;
import java.util.Optional;

public class ORMLiteUsersRepository implements UsersRepository {
    private final UsuarioDAO usuarioDAO;

    public ORMLiteUsersRepository(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Optional<Usuario> findByName(String name) {
        try {
            return usuarioDAO.findByName(name);
        } catch (SQLException ex) {
            System.err.println("Error al buscar usuario por nombre: " + ex.getMessage());
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}