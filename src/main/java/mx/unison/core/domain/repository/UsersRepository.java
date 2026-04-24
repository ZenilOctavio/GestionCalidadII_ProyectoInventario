package mx.unison.core.domain.repository;

import mx.unison.core.domain.models.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Usuario.
 */
public interface UsersRepository {
    /**
     * Recupera todos los usuarios registrados en el sistema.
     * @return Una lista de todos los usuarios.
     */
    List<Usuario> findAll();

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param name El nombre de usuario.
     * @return Un Optional que contiene al usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findByName(String name);

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario El objeto Usuario a crear.
     */
    void create(Usuario usuario);

    /**
     * Actualiza la información de un usuario existente.
     * @param usuario El objeto Usuario con los datos actualizados.
     */
    void update(Usuario usuario);

    /**
     * Elimina un usuario del sistema.
     * @param usuario El objeto Usuario a eliminar.
     */
    void delete(Usuario usuario);
}