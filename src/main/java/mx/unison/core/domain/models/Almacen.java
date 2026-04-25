package mx.unison.core.domain.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Representa un almacén dentro del sistema de inventario.
 * Esta clase se utiliza para mapear la tabla "almacenes" en la base de datos.
 */
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

    @DatabaseField(columnName = "ubicacion", throwIfNull = false, canBeNull = true)
    private String ubicacion;

    /**
     * Constructor por defecto requerido por ORMLite.
     */
    public Almacen() {
        // Constructor sin argumentos requerido por ORMLite
    }

    /**
     * Crea un nuevo almacén con los datos especificados.
     *
     * @param id Identificador único del almacén.
     * @param nombre Nombre del almacén.
     * @param fechaHoraCreacion Fecha y hora de creación del registro.
     * @param fechaHoraUltimaMod Fecha y hora de la última modificación.
     * @param ultimoUsuario Nombre del último usuario que modificó el registro.
     */
    public Almacen(int id, String nombre, String fechaHoraCreacion,
                   String fechaHoraUltimaMod, String ultimoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.fechaHoraUltimaMod = fechaHoraUltimaMod;
        this.ultimoUsuario = ultimoUsuario;
    }

    // Getters
    /**
     * Obtiene el identificador del almacén.
     * @return El ID del almacén.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del almacén.
     * @return El nombre del almacén.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la fecha y hora de creación.
     * @return La fecha de creación en formato String.
     */
    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    /**
     * Obtiene la fecha y hora de la última modificación.
     * @return La fecha de última modificación en formato String.
     */
    public String getFechaHoraUltimaMod() {
        return fechaHoraUltimaMod;
    }

    /**
     * Obtiene el último usuario que realizó una modificación.
     * @return El nombre del usuario.
     */
    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    /**
     * Obtiene la ubicacion del almacen.
     * @return La ubicacion.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    // Setters
    /**
     * Establece el identificador del almacén.
     * @param id El nuevo ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre del almacén.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la fecha y hora de creación.
     * @param fechaHoraCreacion La fecha de creación.
     */
    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    /**
     * Establece la fecha y hora de la última modificación.
     * @param fechaHoraUltimaMod La fecha de modificación.
     */
    public void setFechaHoraUltimaMod(String fechaHoraUltimaMod) {
        this.fechaHoraUltimaMod = fechaHoraUltimaMod;
    }

    /**
     * Establece el último usuario que modificó el registro.
     * @param ultimoUsuario El nombre del usuario.
     */
    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }

    /**
     * Establece la ubicacion.
     * @param ubicacion La ubicacion del almacen.
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // Métodos de compatibilidad con el record anterior
    /**
     * Método de compatibilidad para obtener el ID.
     * @return El ID del almacén.
     */
    public int id() {
        return id;
    }

    /**
     * Método de compatibilidad para obtener el nombre.
     * @return El nombre del almacén.
     */
    public String nombre() {
        return nombre;
    }

    /**
     * Método de compatibilidad para obtener la fecha de creación.
     * @return La fecha de creación.
     */
    public String fechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    /**
     * Método de compatibilidad para obtener la fecha de última modificación.
     * @return La fecha de última modificación.
     */
    public String fechaHoraUltimaMod() {
        return fechaHoraUltimaMod;
    }

    /**
     * Método de compatibilidad para obtener el último usuario.
     * @return El nombre del último usuario.
     */
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