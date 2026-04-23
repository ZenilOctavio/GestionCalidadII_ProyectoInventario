package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;


public class DeleteUsuarioUseCase {
    private final UsersRepository repository;

    public DeleteUsuarioUseCase(UsersRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Usuario usuario) {
        try {
            repository.delete(usuario);
            System.out.println("✓ Usuario eliminado: " + usuario.getNombre());
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}