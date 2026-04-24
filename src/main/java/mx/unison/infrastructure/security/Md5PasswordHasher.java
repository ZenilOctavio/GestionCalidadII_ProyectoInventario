package mx.unison.infrastructure.security;

import mx.unison.core.domain.services.PasswordHasher;

import java.security.MessageDigest;

/**
 * Implementación de PasswordHasher que utiliza el algoritmo MD5.
 * Nota: MD5 no es seguro para producción, pero se usa aquí según los requerimientos legacy.
 */
public class Md5PasswordHasher implements PasswordHasher {

    /**
     * Genera un hash MD5 de la contraseña proporcionada.
     *
     * @param password La contraseña en texto plano.
     * @return El hash MD5 como una cadena hexadecimal.
     * @throws RuntimeException Si el algoritmo MD5 no está disponible o hay error de codificación.
     */
    @Override
    public String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica si la contraseña coincide con el hash MD5.
     *
     * @param password La contraseña en texto plano.
     * @param hashed El hash contra el cual comparar.
     * @return true si coinciden, false en caso contrario.
     */
    @Override
    public boolean verify(String password, String hashed) {
        String hashed2 = hash(password);
        return hashed.contentEquals(hashed2);
    }
}
