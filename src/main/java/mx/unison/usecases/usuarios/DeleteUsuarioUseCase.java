package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;


/**
 * Caso de uso para la eliminación de usuarios del sistema.
 */
public class DeleteUsuarioUseCase {
    private final UsersRepository repository;

    /**
     * Crea una instancia de DeleteUsuarioUseCase.
     * @param repository El repositorio de usuarios.
     */
    public DeleteUsuarioUseCase(UsersRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para eliminar un usuario.
     *
     * @param usuario El objeto Usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
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