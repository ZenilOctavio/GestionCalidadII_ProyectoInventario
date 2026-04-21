package mx.unison.usecases;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;
import mx.unison.presentation.login.LoginService;

import java.util.Optional;

public class LoginUseCase implements LoginService {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    public LoginUseCase(UsersRepository rep, PasswordHasher passwordHasher){
        this.repository = rep;
        this.passwordHasher = passwordHasher;
    }
    @Override
    public boolean withUsernamePassword(String username, String password) {
        Optional<Usuario> userOpt = repository.findByName(username);

        if (userOpt.isEmpty()) return false;

        Usuario user = userOpt.get();

        return passwordHasher.verify(password, user.contrasena());

    }
}
