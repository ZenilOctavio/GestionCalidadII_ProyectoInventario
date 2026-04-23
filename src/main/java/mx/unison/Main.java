package mx.unison;

import javafx.application.Application;
import javafx.stage.Stage;
import mx.unison.core.domain.services.PasswordHasher;
import mx.unison.infrastructure.config.Config;
import mx.unison.infrastructure.persistence.Database;
import mx.unison.infrastructure.persistence.DatabaseSeeder;
import mx.unison.infrastructure.persistence.NoConfigEstablished;
import mx.unison.infrastructure.security.Md5PasswordHasher;
import mx.unison.presentation.app.MainApplication;

import java.sql.SQLException;

public class Main extends Application {

    private MainApplication mainApp;

    @Override
    public void init() throws Exception {
        super.init();
        // Inicializar base de datos ANTES de mostrar la UI
        try {
            Config conf = Config.getInstance();
            Database.setUp(conf);
            Database db = Database.getInstance();

            PasswordHasher passwordHasher = new Md5PasswordHasher();
            DatabaseSeeder seeder = new DatabaseSeeder(db, passwordHasher);
            seeder.seed();

            System.out.println("✓ Base de datos inicializada");
        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar BD: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoConfigEstablished e) {
            System.err.println("✗ Configuración no establecida: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            mainApp = new MainApplication();
            mainApp.start(primaryStage);
        } catch (Exception e) {
            System.err.println("Error al iniciar aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}