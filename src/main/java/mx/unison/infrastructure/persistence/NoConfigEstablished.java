package mx.unison.infrastructure.persistence;

public class NoConfigEstablished extends Exception{
    public NoConfigEstablished(){
        super("Database instance hasn't been initialized");
    }
}
