package mx.unison.presentation.almacenes;

import mx.unison.mocks.almacenes.CreateAlmacenUseCaseMock;
import mx.unison.mocks.almacenes.CreateAlmacenViewMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAlmacenPresenterTest {
    private CreateAlmacenPresenter presenter;
    private CreateAlmacenViewMock view;
    private CreateAlmacenUseCaseMock useCase;
    private boolean saveSuccessCalled = false;

    @BeforeEach
    void setUp() {
        view = new CreateAlmacenViewMock();
        useCase = new CreateAlmacenUseCaseMock();
        // Callback para verificar que la tabla se refresca
        presenter = new CreateAlmacenPresenter(useCase, () -> saveSuccessCalled = true);
        presenter.setView(view);
    }

    @Test
    @DisplayName("CP-18: Crear almacén con nombre vacío")
    void crear_NombreVacio() {
        presenter.onSaveAlmacen("   "); // Solo espacios

        assertTrue(view.errorMostrado(),
                "El sistema debe impedir nombres vacíos tras aplicar el trim().");
        assertFalse(useCase.executeCalled,
                "No se debe llamar al UseCase si el nombre está vacío.");
    }

    @Test
    @DisplayName("CP-19: Crear almacén con nombre menor a 10 caracteres")
    void crear_NombreMuyCorto() {
        presenter.onSaveAlmacen("Bodega");

        assertTrue(view.errorMostrado(),
                "Se debe notificar si el nombre no cumple con el mínimo de 10 caracteres.");
    }

    @Test
    @DisplayName("CP-20: Crear almacén con nombre mayor a 64 caracteres")
    void crear_NombreMuyLargo() {
        String nombreLargo = "A".repeat(65);
        presenter.onSaveAlmacen(nombreLargo);

        assertTrue(view.errorMostrado(),
                "El sistema debe respetar el límite máximo de 64 caracteres.");
    }

    @Test
    @DisplayName("CP-21: Crear almacén con caracteres especiales")
    void crear_NombreCaracteresInvalidos() {
        presenter.onSaveAlmacen("Almacén #1-Principal"); // Tiene # y -

        assertTrue(view.errorMostrado(),
                "La validación alfanumérica debe rechazar símbolos especiales.");
        assertEquals("El nombre solo puede contener letras y números.", view.ultimoMensaje,
                "El mensaje de error de Regex debe ser el correcto.");
    }

    @Test
    @DisplayName("CP-22: Fallo en la base de datos al crear")
    void crear_ErrorEnPersistencia() {
        useCase.resultToReturn = false; // Simulamos que el repositorio devuelve false

        presenter.onSaveAlmacen("Almacen Central de Pruebas");

        assertTrue(view.errorMostrado(),
                "Si el UseCase retorna false, la vista debe mostrar un error de creación.");
        assertFalse(view.fueCerrada,
                "El diálogo no debe cerrarse si el almacén no se guardó.");
    }

    @Test
    @DisplayName("CP-23: Creación de almacén exitosa")
    void crear_FlujoCorrecto() {
        useCase.resultToReturn = true;

        presenter.onSaveAlmacen("Almacen General Hermosillo");

        assertFalse(view.errorMostrado(),
                "No debería haber errores con un nombre válido de 26 caracteres.");
        assertTrue(useCase.executeCalled,
                "El UseCase debe ser invocado con los datos validados.");
        assertTrue(saveSuccessCalled,
                "Se debe ejecutar el refresco de la vista tras el éxito.");
        assertTrue(view.fueCerrada,
                "La ventana de creación debe cerrarse al finalizar con éxito.");
    }
}

