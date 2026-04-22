package mx.unison.presentation;

import mx.unison.infrastructure.data.*;
import mx.unison.infrastructure.persistence.Database;
import mx.unison.infrastructure.persistence.NoConfigEstablished;
import mx.unison.infrastructure.security.Md5PasswordHasher;
import mx.unison.presentation.almacenes.AlmacenPanel;
import mx.unison.presentation.almacenes.AlmacenesPresenter;
import mx.unison.presentation.factories.ViewFactory;
import mx.unison.presentation.home.HomePresenter;
import mx.unison.presentation.login.LoginPresenter;
import mx.unison.presentation.navigation.AppNavigator;
import mx.unison.presentation.productos.ProductosPresenter;
import mx.unison.presentation.home.HomePanel;
import mx.unison.presentation.login.LoginPanel;
import mx.unison.presentation.productos.ProductosPanel;
import mx.unison.usecases.LoginUseCase;
import mx.unison.usecases.almacenes.*;
import mx.unison.usecases.productos.*;

import javax.swing.*;
import java.awt.*;

public class Vistas extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private Database db;

    public void addView(JPanel panel, String name) {
        container.add(panel, name);
    }

    public void showView(String name) {
        cardLayout.show(container, name);
    }

    public Vistas() {
        setTitle("Sistema de Inventario - Cliente");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            db = Database.getInstance();
        }
        catch(NoConfigEstablished ex){
            throw new RuntimeException(ex);
        }

        // Usar los DAOs de la base de datos
        var userRepo = new ORMLiteUsersRepository(db.getUsuarioDAO());
        var productosRepo = new ORMLiteProductsRepository(db.getProductoDAO());
        var almacenesRepo = new ORMLiteAlmacenesRepository(db.getAlmacenDAO());

        var getAllAlmacenesUseCase = new GetAllAlmacenesUseCase(almacenesRepo);

        var navigator = new AppNavigator(this,
                new ViewFactory(
                        new CreateProductoUseCase(productosRepo),
                        new ModifyProductoUseCase(productosRepo),
                        new FindByIdProductoUseCase(productosRepo),
                        new CreateAlmacenUseCase(almacenesRepo),
                        new ModifyAlmacenUseCase(almacenesRepo),
                        new FindByIdAlmacenUseCase(almacenesRepo),
                        getAllAlmacenesUseCase
                )
        );

        var login = new LoginPanel(
                new LoginPresenter(
                        new LoginUseCase(
                                userRepo,
                                new Md5PasswordHasher()
                        ),
                        navigator
                )
        );
        var home = new HomePanel(
                new HomePresenter(navigator)
        );

        var productos = new ProductosPanel(
                new ProductosPresenter(
                        navigator,
                        new GetAllProductosUseCase(productosRepo),
                        new DeleteProductUseCase(productosRepo)
                )
        );

        var almacenes = new AlmacenPanel(
                new AlmacenesPresenter(
                        navigator,
                        getAllAlmacenesUseCase,
                        new DeleteAlmacenUseCase(almacenesRepo)
                )
        );

        container.add(login, AppNavigator.LOGIN);
        container.add(home, AppNavigator.HOME);
        container.add(productos, AppNavigator.PRODUCTOS);
        container.add(almacenes, AppNavigator.ALMACENES);

        add(container);
        navigator.navigateToLogin();
    }
}