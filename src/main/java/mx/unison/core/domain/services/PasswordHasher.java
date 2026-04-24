package mx.unison.core.domain.services;

/**
 * Interfaz que define los servicios de hashing de contraseñas.
 */
public interface PasswordHasher {
    /**
     * Genera un hash a partir de una contraseña en texto plano.
     *
     * @param password La contraseña en texto plano.
     * @return La contraseña hasheada.
     */
    String hash(String password);

    /**
     * Verifica si una contraseña en texto plano coincide con un hash.
     *
     * @param password La contraseña en texto plano.
     * @param hashed El hash contra el cual comparar.
     * @return true si coinciden, false en caso contrario.
     */
    boolean verify(String password, String hashed);
}
