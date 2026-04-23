package mx.unison.presentation.app;

import javafx.stage.Stage;
import mx.unison.infrastructure.data.*;
import mx.unison.infrastructure.persistence.Database;
import mx.unison.infrastructure.persistence.NoConfigEstablished;
import mx.unison.infrastructure.security.Md5PasswordHasher;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.factories.ViewFactory;
import mx.unison.presentation.login.LoginController;
import mx.unison.presentation.home.HomeController;
import mx.unison.presentation.productos.ProductosController;
import mx.unison.presentation.almacenes.AlmacenesController;
import mx.unison.usecases.LoginUseCase;
import mx.unison.usecases.almacenes.*;
import mx.unison.usecases.productos.*;

/**
 * Clase principal que gestiona la inicialización de la aplicación JavaFX
 * Reemplaza a Vistas.java (Swing)
 */
public class MainApplication {
    private Database db;
    private AppNavigatorFX navigator;

    public void start(Stage primaryStage) throws NoConfigEstablished {
        // Obtener instancia de BD
        db = Database.getInstance();

        // Crear repositorios
        var userRepo = new ORMLiteUsersRepository(db.getUsuarioDAO());
        var productosRepo = new ORMLiteProductsRepository(db.getProductoDAO());
        var almacenesRepo = new ORMLiteAlmacenesRepository(db.getAlmacenDAO());

        // Crear casos de uso
        var loginUseCase = new LoginUseCase(userRepo, new Md5PasswordHasher());
        var getAllAlmacenesUseCase = new GetAllAlmacenesUseCase(almacenesRepo);

        // Inicializar navegador
        navigator = new AppNavigatorFX(primaryStage);

        // Crear presentadores y vistas
        LoginController loginController = new LoginController(loginUseCase, navigator);
        HomeController homeController = new HomeController(navigator);
        ProductosController productosController = new ProductosController(
                navigator,
                new GetAllProductosUseCase(productosRepo),
                new DeleteProductUseCase(productosRepo),
                new CreateProductoUseCase(productosRepo),
                new ModifyProductoUseCase(productosRepo),
                new FindByIdProductoUseCase(productosRepo),
                getAllAlmacenesUseCase
        );
        AlmacenesController almacenesController = new AlmacenesController(
                navigator,
                getAllAlmacenesUseCase,
                new DeleteAlmacenUseCase(almacenesRepo),
                new CreateAlmacenUseCase(almacenesRepo),
                new ModifyAlmacenUseCase(almacenesRepo),
                new FindByIdAlmacenUseCase(almacenesRepo)
        );

        // Registrar vistas en navegador
        navigator.registerScene(AppNavigatorFX.LOGIN, loginController.createScene());
        navigator.registerScene(AppNavigatorFX.HOME, homeController.createScene());
        navigator.registerScene(AppNavigatorFX.PRODUCTOS, productosController.createScene());
        navigator.registerScene(AppNavigatorFX.ALMACENES, almacenesController.createScene());

        // Mostrar ventana
        primaryStage.setTitle("Sistema de Inventario - Cliente");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(720);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(e -> onApplicationClose());

        // Navegar al login
        navigator.navigateTo(AppNavigatorFX.LOGIN);
        primaryStage.show();
    }

    private void onApplicationClose() {
        // Cerrar recursos
        if (db != null) {
            db.close();
        }
        System.out.println("✓ Aplicación cerrada");
    }
}