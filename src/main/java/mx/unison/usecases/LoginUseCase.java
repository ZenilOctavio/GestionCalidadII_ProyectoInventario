package mx.unison.usecases;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;
import mx.unison.presentation.login.LoginService;

import java.util.Optional;

public class LoginUseCase implements LoginService {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    public LoginUseCase(UsersRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

    // ✓ Retorna Optional<Usuario>
    public Optional<Usuario> withUsernamePassword(String username, String password) {
        var usuarioOpt = repository.findByName(username);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        var usuario = usuarioOpt.get();
        String hashedPassword = passwordHasher.hash(password);

        if (!usuario.getContrasena().equals(hashedPassword)) {
            return Optional.empty();
        }

        return Optional.of(usuario);
    }
}