package mx.unison.usecases;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;
import mx.unison.presentation.login.LoginService;

import java.util.Optional;

/**
 * Caso de uso para gestionar el inicio de sesión de los usuarios.
 * Implementa la interfaz LoginService para proporcionar validación de credenciales.
 */
public class LoginUseCase implements LoginService {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    /**
     * Crea una instancia de LoginUseCase.
     *
     * @param repository El repositorio de usuarios.
     * @param passwordHasher El servicio de hashing de contraseñas.
     */
    public LoginUseCase(UsersRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Valida las credenciales de un usuario.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña en texto plano.
     * @return Un Optional con el Usuario si las credenciales son válidas, vacío en caso contrario.
     */
    @Override
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