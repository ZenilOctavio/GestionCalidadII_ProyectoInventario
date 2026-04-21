package mx.unison.core.domain.models;

public record Usuario(
        String nombre,
        String rol,
        String contrasena
) {
    public static class Builder {
        private String nombre;
        private String rol;
        private String contrasena;

        public Builder setNombre(String nombre){
            this.nombre = nombre;
            return this;
        }
        public Builder setRol(String rol){
            this.rol = rol;
            return this;
        }

        public Builder setContrasena(String contrasena){
            this.contrasena = contrasena;
            return this;
        }


        public Usuario build(){
            return new Usuario(
                    nombre,
                    rol,
                    contrasena
            );
        }

    }
}
