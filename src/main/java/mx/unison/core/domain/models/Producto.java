package mx.unison.core.domain.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Representa un producto dentro del sistema de inventario.
 * Esta clase se utiliza para mapear la tabla "productos" en la base de datos.
 */
@DatabaseTable(tableName = "productos")
public class Producto {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String nombre;

    @DatabaseField
    private String descripcion;

    @DatabaseField
    private int cantidad;

    @DatabaseField
    private double precio;

    @DatabaseField(columnName = "almacen_id")
    private int almacenId;  // ✓ CAMBIO: int → Integer

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Almacen almacen;

    @DatabaseField(columnName = "fecha_hora_creacion")
    private long fechaCreacion;

    @DatabaseField
    private String departamento;

    @DatabaseField(columnName = "fecha_hora_ultima_modificacion")
    private String fechaModificacion;

    @DatabaseField(columnName = "ultimo_usuario_en_modificar")
    private String ultimoUsuario;

    /**
     * Constructor por defecto requerido por ORMLite.
     */
    public Producto() {
        // Constructor sin argumentos requerido por ORMLite
    }

    /**
     * Crea un nuevo producto con los datos especificados.
     *
     * @param id Identificador único del producto.
     * @param nombre Nombre del producto.
     * @param descripcion Descripción detallada del producto.
     * @param cantidad Cantidad disponible en inventario.
     * @param precio Precio unitario del producto.
     * @param almacenId Identificador del almacén al que pertenece.
     * @param fechaCreacion Fecha de creación en formato timestamp largo.
     * @param departamento Departamento al que pertenece el producto.
     * @param fechaModificacion Fecha de la última modificación.
     * @param ultimoUsuario Último usuario que modificó el registro.
     */
    public Producto(int id, String nombre, String descripcion, int cantidad, double precio,
                    int almacenId, long fechaCreacion, String departamento,
                    String fechaModificacion, String ultimoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.almacenId = almacenId;
        this.fechaCreacion = fechaCreacion;
        this.departamento = departamento;
        this.fechaModificacion = fechaModificacion;
        this.ultimoUsuario = ultimoUsuario;
    }

    // Getters
    /**
     * Obtiene el identificador del producto.
     * @return El ID del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la descripción del producto.
     * @return La descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la cantidad en stock.
     * @return La cantidad.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene el precio del producto.
     * @return El precio.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Obtiene el ID del almacén asociado.
     * @return El ID del almacén.
     */
    public Integer getAlmacenId() {  // ✓ CAMBIO: retorna Integer
        return almacenId;
    }

    /**
     * Obtiene la fecha de creación.
     * @return La fecha en formato long.
     */
    public long getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Obtiene el departamento del producto.
     * @return El nombre del departamento.
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Obtiene la fecha de la última modificación.
     * @return La fecha de modificación.
     */
    public String getFechaModificacion() {
        return fechaModificacion;
    }

    /**
     * Obtiene el último usuario que modificó el producto.
     * @return El nombre del usuario.
     */
    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    // Setters
    /**
     * Establece el identificador del producto.
     * @param id El nuevo ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la descripción del producto.
     * @param descripcion La nueva descripción.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Establece la cantidad en stock.
     * @param cantidad La nueva cantidad.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Establece el precio del producto.
     * @param precio El nuevo precio.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Establece el ID del almacén asociado.
     * @param almacenId El nuevo ID de almacén.
     */
    public void setAlmacenId(Integer almacenId) {  // ✓ CAMBIO: recibe Integer
        this.almacenId = almacenId;
    }

    /**
     * Establece la fecha de creación.
     * @param fechaCreacion La nueva fecha de creación.
     */
    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Establece el departamento del producto.
     * @param departamento El nuevo departamento.
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Establece la fecha de última modificación.
     * @param fechaModificacion La nueva fecha de modificación.
     */
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    /**
     * Establece el último usuario que modificó el registro.
     * @param ultimoUsuario El nombre del usuario.
     */
    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }

    // Métodos de compatibilidad con el record anterior
    /**
     * Método de compatibilidad para obtener el ID.
     * @return El ID del producto.
     */
    public int id() {
        return id;
    }

    /**
     * Método de compatibilidad para obtener el nombre.
     * @return El nombre del producto.
     */
    public String nombre() {
        return nombre;
    }

    /**
     * Método de compatibilidad para obtener la descripción.
     * @return La descripción del producto.
     */
    public String descripcion() {
        return descripcion;
    }

    /**
     * Método de compatibilidad para obtener la cantidad.
     * @return La cantidad.
     */
    public int cantidad() {
        return cantidad;
    }

    /**
     * Método de compatibilidad para obtener el precio.
     * @return El precio.
     */
    public double precio() {
        return precio;
    }

    /**
     * Método de compatibilidad para obtener el ID del almacén.
     * @return El ID del almacén.
     */
    public int almacenId() {  // ✓ CAMBIO: retorna int para compatibilidad
        return almacenId;
    }

    /**
     * Método de compatibilidad para obtener la fecha de creación.
     * @return La fecha de creación.
     */
    public long fechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Método de compatibilidad para obtener el departamento.
     * @return El departamento.
     */
    public String departamento() {
        return departamento;
    }

    /**
     * Método de compatibilidad para obtener la fecha de modificación.
     * @return La fecha de modificación.
     */
    public String fechaModificacion() {
        return fechaModificacion;
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
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }

    /**
     * Obtiene el objeto Almacen asociado.
     * @return El almacén asociado.
     */
    public Almacen getAlmacen() {
        return almacen;
    }
}