package mx.unison.core.domain.models;

public record Producto (int id,
    String nombre,
    String descripcion,
    int cantidad,
    double precio,
    int almacenId,
    int fechaCreacion,
    String departamento,
    String fechaModificacion,
    String ultimoUsuario
) {
    public static class Builder {
        public int id;
        public String nombre;
        public String descripcion;
        public int cantidad;
        public double precio;
        public int almacenId;
        public int fechaCreacion;
        public String fechaModificacion;
        public String departamento;
        public String ultimoUsuario;

        public Builder setId(int id){
            this.id = id;
            return this;
        }
        public Builder setNombre(String nombre){
            this.nombre = nombre;
            return this;
        }
        public Builder setDescripcion(String descripcion){
            this.descripcion = descripcion;
            return this;
        }
        public Builder setCantidad(int cantidad){
            this.cantidad = cantidad;
            return this;
        }
        public Builder setPrecio(double precio){
            this.precio = precio;
            return this;
        }
        public Builder setAlmacenId(int almacenId){
            this.almacenId = almacenId;
            return this;
        }
        public Builder setFechaCreacion(int fechaCreacion){
            this.fechaCreacion = fechaCreacion;
            return this;
        }
        public Builder setFechaModificacion(String fechaModificacion){
            this.fechaModificacion = fechaModificacion;
            return this;
        }
        public Builder setUltimoUsuario(String ultimoUsuario){
            this.ultimoUsuario = ultimoUsuario;
            return this;
        }
        public Builder setDepartamento(String departamento){
            this.departamento = departamento;
            return this;
        }

        public Producto build(){
            return new Producto(
                    id,
                    nombre,
                    descripcion,
                    cantidad,
                    precio,
                    almacenId,
                    fechaCreacion,
                    departamento,
                    fechaModificacion,
                    ultimoUsuario
            );
        }

    }

}
