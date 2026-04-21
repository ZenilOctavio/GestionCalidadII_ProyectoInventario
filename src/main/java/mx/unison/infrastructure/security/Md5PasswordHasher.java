package mx.unison.infrastructure.security;

import mx.unison.core.domain.services.PasswordHasher;

import java.security.MessageDigest;

public class Md5PasswordHasher implements PasswordHasher {

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

    @Override
    public boolean verify(String password, String hashed) {
        String hashed2 = hash(password);
        return hashed.contentEquals(hashed2);
    }
}
