package mx.unison.infrastructure.persistence;

/**
 * Excepción lanzada cuando se intenta acceder a la base de datos sin haberla configurado previamente.
 */
public class NoConfigEstablished extends Exception{
    /**
     * Crea una nueva instancia de la excepción con un mensaje por defecto.
     */
    public NoConfigEstablished(){
        super("Database instance hasn't been initialized");
    }
}
