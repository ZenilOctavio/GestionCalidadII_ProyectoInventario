package mx.unison.core.domain.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

    public Producto() {
        // Constructor sin argumentos requerido por ORMLite
    }

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
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public Integer getAlmacenId() {  // ✓ CAMBIO: retorna Integer
        return almacenId;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setAlmacenId(Integer almacenId) {  // ✓ CAMBIO: recibe Integer
        this.almacenId = almacenId;
    }

    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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

    public String descripcion() {
        return descripcion;
    }

    public int cantidad() {
        return cantidad;
    }

    public double precio() {
        return precio;
    }

    public int almacenId() {  // ✓ CAMBIO: retorna int para compatibilidad
        return almacenId;
    }

    public long fechaCreacion() {
        return fechaCreacion;
    }

    public String departamento() {
        return departamento;
    }

    public String fechaModificacion() {
        return fechaModificacion;
    }

    public String ultimoUsuario() {
        return ultimoUsuario;
    }

    public Almacen almacen(){
        return almacen;
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
}