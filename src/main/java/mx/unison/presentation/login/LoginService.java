package mx.unison.presentation.login;

import mx.unison.core.domain.models.Usuario;

import java.util.Optional;

public interface LoginService {
    Optional<Usuario> withUsernamePassword(String username, String password);
}
