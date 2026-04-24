package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;

import java.util.List;

/**
 * Caso de uso para obtener la lista completa de usuarios del sistema.
 */
public class GetAllUsuariosUseCase {
    private final UsersRepository repository;

    /**
     * Crea una instancia de GetAllUsuariosUseCase.
     * @param repository El repositorio de usuarios.
     */
    public GetAllUsuariosUseCase(UsersRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la lógica para recuperar todos los usuarios.
     * @return Una lista de todos los usuarios registrados.
     */
    public List<Usuario> execute() {
        return repository.findAll();
    }
}