package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    List<Usuario> findAll();
    Optional<Usuario> findByName(String name);
    void create(Usuario usuario);
    void update(Usuario usuario);
    void delete(Usuario usuario);
}