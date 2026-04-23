package mx.unison.mocks;

import mx.unison.core.domain.models.Usuario;
import mx.unison.presentation.login.LoginService;

import java.util.Optional;

public class LoginUseCaseMock implements LoginService {
    public boolean shouldFail = false;

    @Override
    public Optional<Usuario> withUsernamePassword(String username, String password) {
        if (shouldFail)
            return Optional.empty();

        return Optional.of(new Usuario());
    }
}

