package mx.unison.presentation.session;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.models.UserRole;

/**
 * Singleton encargado de gestionar la sesión del usuario activo en la aplicación.
 * Proporciona un punto de acceso global para consultar el usuario autenticado,
 * su rol y verificar los permisos de acceso a las diferentes funcionalidades
 * del sistema (CRUD de productos, almacenes y usuarios).
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario currentUser;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private SessionManager() {
    }

    /**
     * Obtiene la instancia única del gestor de sesión.
     * 
     * @return La instancia de {@link SessionManager}.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Establece el usuario actual tras una autenticación exitosa.
     * 
     * @param usuario El objeto {@link Usuario} que ha iniciado sesión.
     */
    public void login(Usuario usuario) {
        this.currentUser = usuario;
        System.out.println("✓ Usuario autenticado: " + usuario.getNombre() + " - Rol: " + usuario.getRol());
    }

    /**
     * Finaliza la sesión actual y limpia los datos del usuario.
     */
    public void logout() {
        System.out.println("✓ Usuario desconectado: " + (currentUser != null ? currentUser.getNombre() : "N/A"));
        this.currentUser = null;
    }

    /**
     * @return El objeto {@link Usuario} en sesión, o null si no hay ninguno.
     */
    public Usuario getCurrentUser() {
        return currentUser;
    }

    /**
     * Obtiene el rol del usuario actual mapeado a la enumeración de dominio.
     * 
     * @return El {@link UserRole} correspondiente al usuario actual.
     */
    public UserRole getCurrentRole() {
        if (currentUser == null) {
            return null;
        }
        return UserRole.fromString(currentUser.getRol());
    }

    /**
     * Verifica si existe una sesión activa.
     * 
     * @return true si un usuario ha iniciado sesión.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Verifica si el usuario actual tiene permisos para gestionar cuentas de usuario.
     * 
     * @return true si el rol permite la gestión de usuarios.
     */
    public boolean canManageUsers() {
        return isLoggedIn() && getCurrentRole().canManageUsers();
    }

    /**
     * Verifica si el usuario actual tiene permisos para gestionar el catálogo de productos.
     * 
     * @return true si el rol permite la gestión de productos.
     */
    public boolean canManageProducts() {
        return isLoggedIn() && getCurrentRole().canManageProducts();
    }

    /**
     * Verifica si el usuario actual tiene permisos para gestionar almacenes.
     * 
     * @return true si el rol permite la gestión de almacenes.
     */
    public boolean canManageWarehouses() {
        return isLoggedIn() && getCurrentRole().canManageWarehouses();
    }
}