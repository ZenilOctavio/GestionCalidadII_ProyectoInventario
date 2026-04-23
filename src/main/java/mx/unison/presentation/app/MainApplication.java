package mx.unison.presentation.app;

import javafx.stage.Stage;
import mx.unison.infrastructure.data.*;
import mx.unison.infrastructure.persistence.Database;
import mx.unison.infrastructure.persistence.NoConfigEstablished;
import mx.unison.infrastructure.security.Md5PasswordHasher;
import mx.unison.presentation.navigation.AppNavigatorFX;
import mx.unison.presentation.login.LoginController;
import mx.unison.presentation.home.HomeController;
import mx.unison.presentation.productos.ProductosController;
import mx.unison.presentation.almacenes.AlmacenesController;
import mx.unison.presentation.usuarios.UsuariosController;
import mx.unison.usecases.LoginUseCase;
import mx.unison.usecases.almacenes.*;
import mx.unison.usecases.productos.*;
import mx.unison.usecases.usuarios.*;

/**
 * Clase principal que gestiona la inicialización de la aplicación JavaFX
 */
public class MainApplication {
    private Database db;
    private AppNavigatorFX navigator;

    public void start(Stage primaryStage) throws NoConfigEstablished {
        db = Database.getInstance();

        var userRepo = new ORMLiteUsuariosRepository(db.getUsuarioDAO());
        var usuariosRepo = new ORMLiteUsuariosRepository(db.getUsuarioDAO());
        var productosRepo = new ORMLiteProductsRepository(db.getProductoDAO());
        var almacenesRepo = new ORMLiteAlmacenesRepository(db.getAlmacenDAO());

        var passwordHasher = new Md5PasswordHasher();

        var loginUseCase = new LoginUseCase(userRepo, passwordHasher);
        var getAllAlmacenesUseCase = new GetAllAlmacenesUseCase(almacenesRepo);
        var createAlmacenUseCase = new CreateAlmacenUseCase(almacenesRepo);
        var modifyAlmacenUseCase = new ModifyAlmacenUseCase(almacenesRepo);
        var findByIdAlmacenUseCase = new FindByIdAlmacenUseCase(almacenesRepo);
        var deleteAlmacenUseCase = new DeleteAlmacenUseCase(almacenesRepo);

        var getAllProductosUseCase = new GetAllProductosUseCase(productosRepo);
        var createProductoUseCase = new CreateProductoUseCase(productosRepo);
        var modifyProductoUseCase = new ModifyProductoUseCase(productosRepo);
        var findByIdProductoUseCase = new FindByIdProductoUseCase(productosRepo);
        var deleteProductUseCase = new DeleteProductUseCase(productosRepo);

        var getAllUsuariosUseCase = new GetAllUsuariosUseCase(usuariosRepo);
        var createUsuarioUseCase = new CreateUsuarioUseCase(usuariosRepo, passwordHasher);
        var modifyUsuarioUseCase = new ModifyUsuarioUseCase(usuariosRepo, passwordHasher);
        var deleteUsuarioUseCase = new DeleteUsuarioUseCase(usuariosRepo);

        navigator = new AppNavigatorFX(primaryStage);

        LoginController loginController = new LoginController(loginUseCase, navigator);
        HomeController homeController = new HomeController(navigator);
        ProductosController productosController = new ProductosController(
                navigator,
                getAllProductosUseCase,
                deleteProductUseCase,
                createProductoUseCase,
                modifyProductoUseCase,
                findByIdProductoUseCase,
                getAllAlmacenesUseCase
        );
        AlmacenesController almacenesController = new AlmacenesController(
                navigator,
                getAllAlmacenesUseCase,
                deleteAlmacenUseCase,
                createAlmacenUseCase,
                modifyAlmacenUseCase,
                findByIdAlmacenUseCase
        );
        UsuariosController usuariosController = new UsuariosController(
                navigator,
                getAllUsuariosUseCase,
                deleteUsuarioUseCase,
                createUsuarioUseCase,
                modifyUsuarioUseCase
        );

        // ✓ Registrar HOME también con controlador
        navigator.registerSceneWithController(AppNavigatorFX.LOGIN, loginController.createScene(), loginController);
        navigator.registerSceneWithController(AppNavigatorFX.HOME, homeController.createScene(), homeController);
        navigator.registerSceneWithController(AppNavigatorFX.PRODUCTOS, productosController.createScene(), productosController);
        navigator.registerSceneWithController(AppNavigatorFX.ALMACENES, almacenesController.createScene(), almacenesController);
        navigator.registerSceneWithController(AppNavigatorFX.USUARIOS, usuariosController.createScene(), usuariosController);

        primaryStage.setTitle("Sistema de Inventario - Cliente");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(720);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(e -> onApplicationClose());

        navigator.navigateTo(AppNavigatorFX.LOGIN);
        primaryStage.show();
    }

    private void onApplicationClose() {
        if (db != null) {
            db.close();
        }
        System.out.println("✓ Aplicación cerrada");
    }
}