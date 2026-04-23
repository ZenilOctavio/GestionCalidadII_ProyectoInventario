package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.infrastructure.persistence.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class SQLiteUsersRepository implements UsersRepository {

    private final Database db;

    public SQLiteUsersRepository(Database db) {
        this.db = db;
    }

    @Override
    public List<Usuario> findAll() {
        return List.of();
    }

    @Override
    public Optional<Usuario> findByName(String name) {
        String sql = "SELECT nombre, password, rol FROM usuarios WHERE nombre = ?";
        try {
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, name);

            ResultSet set = stmt.executeQuery();
            if (!set.next()) return Optional.empty();


            var usuario = new Usuario();
            usuario.setRol(set.getString("rol"));
            usuario.setContrasena(set.getString("password"));
            usuario.setNombre(set.getString("nombre"));

            return Optional.of(usuario);

        }
        catch(SQLException ex){
            System.out.println("Error repositorio");
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
            return Optional.empty();
        }

    }

    @Override
    public void create(Usuario usuario) {

    }

    @Override
    public void update(Usuario usuario) {

    }

    @Override
    public void delete(Usuario usuario) {

    }
}
