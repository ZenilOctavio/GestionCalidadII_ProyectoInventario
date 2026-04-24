package mx.unison.core.domain.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Representa un usuario dentro del sistema.
 * Esta clase se utiliza para mapear la tabla "usuarios" en la base de datos.
 */
@DatabaseTable(tableName = "usuarios")
public class Usuario {
    @DatabaseField(id = true)
    private String nombre;

    @DatabaseField
    private String contrasena;

    @DatabaseField
    private String rol;

    /**
     * Constructor por defecto requerido por ORMLite.
     */
    public Usuario() {
        // Constructor sin argumentos requerido por ORMLite
    }

    /**
     * Crea un nuevo usuario con los datos especificados.
     *
     * @param nombre Nombre de usuario (identificador único).
     * @param contrasena Contraseña (generalmente hasheada).
     * @param rol Rol asignado al usuario (e.g., ADMIN, PRODUCTOS, ALMACEN).
     */
    public Usuario(String nombre, String contrasena, String rol) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters
    /**
     * Obtiene el nombre del usuario.
     * @return El nombre de usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña (hasheada).
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Obtiene el rol del usuario.
     * @return El rol del usuario.
     */
    public String getRol() {
        return rol;
    }

    // Setters
    /**
     * Establece el nombre del usuario.
     * @param nombre El nuevo nombre de usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contrasena La nueva contraseña.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Establece el rol del usuario.
     * @param rol El nuevo rol.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método para compatibilidad con el método contrasena() del record anterior
    /**
     * Método de compatibilidad para obtener la contraseña.
     * @return La contraseña.
     */
    public String contrasena() {
        return this.contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}