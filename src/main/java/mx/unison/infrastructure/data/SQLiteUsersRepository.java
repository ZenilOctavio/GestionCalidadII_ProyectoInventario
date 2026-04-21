package mx.unison.infrastructure.data;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.infrastructure.persistence.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Optional;

public class SQLiteUsersRepository implements UsersRepository {

    private final Database db;

    public SQLiteUsersRepository(Database db) {
        this.db = db;
    }

    @Override
    public Optional<Usuario> findByName(String name) {
        String sql = "SELECT nombre, password, rol FROM usuarios WHERE nombre = ?";
        try {
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, name);

            ResultSet set = stmt.executeQuery();
            if (!set.next()) return Optional.empty();


            Usuario.Builder builder = new Usuario.Builder();
            builder.setRol(set.getString("rol"));
            builder.setContrasena(set.getString("password"));
            builder.setNombre(set.getString("nombre"));

            return Optional.of(builder.build());

        }
        catch(SQLException ex){
            System.out.println("Error repositorio");
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
            return Optional.empty();
        }

    }
}
