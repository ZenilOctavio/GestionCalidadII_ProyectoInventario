package mx.unison.core.domain.models;

/**
 * Enum que define los roles disponibles en el sistema
 */
public enum UserRole {
    ADMIN("ADMIN"),
    PRODUCTOS("PRODUCTOS"),
    ALMACEN("ALMACEN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convierte un String a UserRole
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
     * Verifica si el usuario puede gestionar usuarios
     */
    public boolean canManageUsers() {
        return this == ADMIN;
    }

    /**
     * Verifica si el usuario puede gestionar productos
     */
    public boolean canManageProducts() {
        return this == ADMIN || this == PRODUCTOS;
    }

    /**
     * Verifica si el usuario puede gestionar almacenes
     */
    public boolean canManageWarehouses() {
        return this == ADMIN || this == ALMACEN;
    }
}