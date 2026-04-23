package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;

public class CreateUsuarioUseCase {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    public CreateUsuarioUseCase(UsersRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

    public boolean execute(String nombre, String contrasena, String rol) {
        // Validar que el usuario no exista
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