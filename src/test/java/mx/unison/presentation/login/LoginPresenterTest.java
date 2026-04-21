package mx.unison.presentation.login;

import mx.unison.mocks.AppNavigatorMock;
import mx.unison.mocks.LoginUseCaseMock;
import mx.unison.mocks.LoginViewMock;
import mx.unison.presentation.navigation.Navigator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LoginPresenterTest {
    private LoginPresenter presenter;
    private LoginViewMock view;
    private LoginUseCaseMock useCase;
    private AppNavigatorMock navigator;

    @BeforeEach
    void setUp() {
        view = new LoginViewMock();
        useCase = new LoginUseCaseMock();
        navigator = new AppNavigatorMock();

        presenter = new LoginPresenter(useCase, navigator);
        presenter.setView(view);
    }

    @Test
    @DisplayName("CP-01: Login con usuario vacío")
    void login_UsuarioVacio() {
        presenter.onLoginClicked("", "password123");

        assertTrue(view.seLlamoAError(),
                "El sistema debería notificar un error cuando el campo de usuario se envía vacío.");
        assertNotNull(view.getUltimoMensaje(),
                "La vista debería haber recibido un mensaje descriptivo para el error de usuario vacío.");
    }

    @Test
    @DisplayName("CP-02: Login con contraseña vacía")
    void login_PasswordVacio() {
        presenter.onLoginClicked("octavio", "");

        assertTrue(view.seLlamoAError(),
                "Se esperaba una llamada a showError al intentar ingresar sin contraseña.");
    }

    @Test
    @DisplayName("CP-03: Login con credenciales incorrectas (Lógica de Negocio)")
    void login_CredencialesErroneas() {
        useCase.shouldFail = true; // Forzamos fallo en el UseCase

        presenter.onLoginClicked("admin", "wrong_pass");

        assertTrue(view.seLlamoAError(),
                "El Presenter debe propagar el error a la vista cuando las credenciales no son válidas.");
    }

    @Test
    @DisplayName("CP-04: Login con diferencias de capitalización (Sensibilidad)")
    void login_Capitalizacion() {
        // En este test verificamos que el Presenter envíe los datos al UseCase.
        // La lógica de si "OCTAVIO" == "octavio" vive en el UseCase/Repo.
        presenter.onLoginClicked("OCTAVIO", "Admin123");

        assertEquals(0, view.getTotalErrores(),
                "No debería mostrarse error en el Presenter por capitalización; esa lógica es del dominio.");
    }

    @Test
    @DisplayName("CP-05: Login con credenciales correctas (Flujo de Éxito)")
    void login_Exitoso() {
        useCase.shouldFail = false;

        presenter.onLoginClicked("octavio", "admin123");

        assertEquals(0, view.getTotalErrores(),
                "Un login con datos válidos no debe generar ninguna alerta de error.");

        assertTrue(navigator.navegoHome,
                "El sistema debe redirigir al panel principal tras una autenticación exitosa.");
    }
}