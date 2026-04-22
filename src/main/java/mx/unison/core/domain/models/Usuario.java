package mx.unison.core.domain.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "usuarios")
public class Usuario {
    @DatabaseField(id = true)
    private String nombre;

    @DatabaseField
    private String contrasena;

    @DatabaseField
    private String rol;

    public Usuario() {
        // Constructor sin argumentos requerido por ORMLite
    }

    public Usuario(String nombre, String contrasena, String rol) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método para compatibilidad con el método contrasena() del record anterior
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