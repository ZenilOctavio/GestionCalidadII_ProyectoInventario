package mx.unison.core.domain.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "almacenes")
public class Almacen {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String nombre;

    @DatabaseField(columnName = "fecha_hora_creacion")
    private String fechaHoraCreacion;

    @DatabaseField(columnName = "fecha_hora_ultima_modificacion")
    private String fechaHoraUltimaMod;

    @DatabaseField(columnName = "ultimo_usuario_en_modificar")
    private String ultimoUsuario;

    public Almacen() {
        // Constructor sin argumentos requerido por ORMLite
    }

    public Almacen(int id, String nombre, String fechaHoraCreacion,
                   String fechaHoraUltimaMod, String ultimoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.fechaHoraUltimaMod = fechaHoraUltimaMod;
        this.ultimoUsuario = ultimoUsuario;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public String getFechaHoraUltimaMod() {
        return fechaHoraUltimaMod;
    }

    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public void setFechaHoraUltimaMod(String fechaHoraUltimaMod) {
        this.fechaHoraUltimaMod = fechaHoraUltimaMod;
    }

    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }

    // Métodos de compatibilidad con el record anterior
    public int id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }

    public String fechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public String fechaHoraUltimaMod() {
        return fechaHoraUltimaMod;
    }

    public String ultimoUsuario() {
        return ultimoUsuario;
    }

    @Override
    public String toString() {
        return "Almacen{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}