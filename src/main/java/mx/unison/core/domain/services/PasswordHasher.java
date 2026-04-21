package mx.unison.core.domain.services;

public interface PasswordHasher {
    String hash(String password);
    boolean verify(String password, String hashed);
}
