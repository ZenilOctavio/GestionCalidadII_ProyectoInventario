package mx.unison.presentation.session;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.models.UserRole;

/**
 * Gestiona la sesión del usuario actual
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Usuario usuario) {
        this.currentUser = usuario;
        System.out.println("✓ Usuario autenticado: " + usuario.getNombre() + " - Rol: " + usuario.getRol());
    }

    public void logout() {
        System.out.println("✓ Usuario desconectado: " + (currentUser != null ? currentUser.getNombre() : "N/A"));
        this.currentUser = null;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public UserRole getCurrentRole() {
        if (currentUser == null) {
            return null;
        }
        return UserRole.fromString(currentUser.getRol());
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean canManageUsers() {
        return isLoggedIn() && getCurrentRole().canManageUsers();
    }

    public boolean canManageProducts() {
        return isLoggedIn() && getCurrentRole().canManageProducts();
    }

    public boolean canManageWarehouses() {
        return isLoggedIn() && getCurrentRole().canManageWarehouses();
    }
}