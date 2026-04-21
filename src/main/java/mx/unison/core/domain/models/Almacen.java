package mx.unison.core.domain.models;

public record Almacen(
    int id,
    String nombre,
    String fechaHoraCreacion,
    String fechaHoraUltimaMod,
    String ultimoUsuario
){
    public static class Builder {
        private int id;
        private String nombre;
        private String fechaHoraCreacion;
        private String fechaHoraUltimaMod;
        private String ultimoUsuario;

        public Builder setId(int id){
            this.id = id;
            return this;
        }
        public Builder setNombre(String nombre){
            this.nombre = nombre;
            return this;
        }
        public Builder setFechaHoraCreacion(String fechaHoraCreacion){
            this.fechaHoraCreacion = fechaHoraCreacion;
            return this;
        }
        public Builder setFechaHoraUltimaMod(String fechaHoraUltimaMod) {
            this.fechaHoraUltimaMod = fechaHoraUltimaMod;
            return this;
        }
        public Builder setUltimoUsuario(String ultimoUsuario) {
            this.ultimoUsuario = ultimoUsuario;
            return this;
        }

        public Almacen build(){
            return new Almacen(
                    id,
                    nombre,
                    fechaHoraCreacion,
                    fechaHoraUltimaMod,
                    ultimoUsuario
            );
        }

    }
}
