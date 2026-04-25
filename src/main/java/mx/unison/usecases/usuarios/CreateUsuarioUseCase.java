package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;

/**
 * Caso de uso para la creación de nuevos usuarios en el sistema.
 */
public class CreateUsuarioUseCase {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    /**
     * Crea una instancia de CreateUsuarioUseCase.
     *
     * @param repository El repositorio de usuarios.
     * @param passwordHasher El servicio de hashing de contraseñas.
     */
    public CreateUsuarioUseCase(UsersRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Ejecuta la lógica para crear un nuevo usuario.
     *
     * @param nombre El nombre del nuevo usuario.
     * @param contrasena La contraseña en texto plano.
     * @param rol El rol asignado al usuario.
     * @return true si el usuario se creó correctamente, false si ya existe o hubo un error.
     */
    public boolean execute(String nombre, String contrasena, String rol) {

        if (nombre == null || contrasena == null || rol == null) {
            throw new RuntimeException("Los datos del usuario no pueden ser nulos");
        }


        if (nombre.trim().isEmpty()) {
            System.err.println("Error: El nombre no puede estar vacío");
            return false;
        }

        if (contrasena.length() < 8) {
            System.err.println("Error: La contraseña debe tener al menos 8 caracteres");
            return false;
        }

        if (nombre.length() > 100) {
            System.err.println("Error: El nombre es demasiado largo");
            return false;
        }


        if (repository.findByName(nombre).isPresent()) {
            System.err.println("Usuario ya existe: " + nombre);
            return false;
        }

        String contrasenaHasheada = passwordHasher.hash(contrasena);
        Usuario nuevoUsuario = new Usuario(nombre, contrasenaHasheada, rol);

        try {
            repository.create(nuevoUsuario);
            System.out.println("✓ Usuario creado: " + nombre);
            return true;
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }
}