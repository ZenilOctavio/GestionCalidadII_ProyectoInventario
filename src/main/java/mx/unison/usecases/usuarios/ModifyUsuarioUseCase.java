package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;

/**
 * Caso de uso para la modificación de usuarios existentes.
 */
public class ModifyUsuarioUseCase {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    /**
     * Crea una instancia de ModifyUsuarioUseCase.
     *
     * @param repository El repositorio de usuarios.
     * @param passwordHasher El servicio de hashing de contraseñas.
     */
    public ModifyUsuarioUseCase(UsersRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Ejecuta la lógica para modificar un usuario.
     *
     * @param nombre El nombre del usuario a modificar.
     * @param contrasena La nueva contraseña (opcional, puede ser null o vacía).
     * @param rol El nuevo rol asignado.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean execute(String nombre, String contrasena, String rol) {
        var usuarioOpt = repository.findByName(nombre);
        if (usuarioOpt.isEmpty()) {
            System.err.println("Usuario no encontrado: " + nombre);
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setRol(rol);

        if (contrasena != null && !contrasena.isEmpty()) {
            usuario.setContrasena(passwordHasher.hash(contrasena));
        }

        try {
            repository.update(usuario);
            System.out.println("✓ Usuario modificado: " + nombre);
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }
}