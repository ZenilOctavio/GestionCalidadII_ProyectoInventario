package mx.unison.usecases.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;

public class ModifyUsuarioUseCase {
    private final UsersRepository repository;
    private final PasswordHasher passwordHasher;

    public ModifyUsuarioUseCase(UsersRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

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