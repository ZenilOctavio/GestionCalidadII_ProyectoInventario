package mx.unison.core.domain.models;

/**
 * Enum que define los roles disponibles en el sistema y sus permisos.
 */
public enum UserRole {
    ADMIN("ADMIN"),
    PRODUCTOS("PRODUCTOS"),
    ALMACEN("ALMACEN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    /**
     * Obtiene el valor textual del rol.
     * @return El valor del rol como String.
     */
    public String getValue() {
        return value;
    }

    /**
     * Convierte una cadena de texto a un objeto UserRole.
     *
     * @param value El valor textual del rol.
     * @return El UserRole correspondiente.
     * @throws IllegalArgumentException Si el valor no coincide con ningún rol válido.
     */
    public static UserRole fromString(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol no válido: " + value);
    }

    /**
     * Verifica si el rol tiene permisos para gestionar usuarios.
     * @return true si puede gestionar usuarios, false en caso contrario.
     */
    public boolean canManageUsers() {
        return this == ADMIN;
    }

    /**
     * Verifica si el rol tiene permisos para gestionar productos.
     * @return true si puede gestionar productos, false en caso contrario.
     */
    public boolean canManageProducts() {
        return this == ADMIN || this == PRODUCTOS;
    }

    /**
     * Verifica si el rol tiene permisos para gestionar almacenes.
     * @return true si puede gestionar almacenes, false en caso contrario.
     */
    public boolean canManageWarehouses() {
        return this == ADMIN || this == ALMACEN;
    }
}