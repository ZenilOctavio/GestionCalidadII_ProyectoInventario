package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;

import java.util.List;

public class GetAllUsuariosUseCase {
    private final UsersRepository repository;

    public GetAllUsuariosUseCase(UsersRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> execute() {
        return repository.findAll();
    }
}