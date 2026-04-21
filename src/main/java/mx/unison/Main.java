package mx.unison;

import mx.unison.core.domain.services.PasswordHasher;
import mx.unison.infrastructure.config.Config;
import mx.unison.infrastructure.persistence.Database;
import mx.unison.infrastructure.persistence.DatabaseSeeder;
import mx.unison.infrastructure.persistence.NoConfigEstablished;
import mx.unison.infrastructure.security.Md5PasswordHasher;
import mx.unison.presentation.Vistas;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Config conf = Config.getInstance();
        Database db;
        DatabaseSeeder seeder;
        PasswordHasher passwordHasher = new Md5PasswordHasher();

        try {Database.setUp(conf);}
        catch(SQLException ex){
            return;
        }

        try {db = Database.getInstance();} catch (NoConfigEstablished e) {
            throw new RuntimeException(e);
        }
        try {seeder = new DatabaseSeeder(db, passwordHasher);} catch (SQLException e) {
            throw new RuntimeException(e);
        }

        seeder.seed();


        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            Vistas vistas = new Vistas();
            vistas.setVisible(true);
        });
    }
}
