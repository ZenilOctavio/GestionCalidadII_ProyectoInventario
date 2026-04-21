package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Usuario;

import java.util.Optional;

public interface UsersRepository {
    Optional<Usuario> findByName(String name);
}
