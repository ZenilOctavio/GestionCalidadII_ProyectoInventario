package mx.unison.presentation.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.mocks.almacenes.FindByIdAlmacenMock;
import mx.unison.mocks.almacenes.ModifyAlmacenUseCaseMock;
import mx.unison.mocks.almacenes.ModifyAlmacenViewMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModifyAlmacenPresenterTest {
    private ModifyAlmacenPresenter presenter;
    private ModifyAlmacenViewMock view;
    private ModifyAlmacenUseCaseMock modifyUseCase;
    private FindByIdAlmacenMock findUseCase;
    private boolean saveSuccessCalled = false;

    @BeforeEach
    void setUp() {
        view = new ModifyAlmacenViewMock();
        modifyUseCase = new ModifyAlmacenUseCaseMock();
        findUseCase = new FindByIdAlmacenMock();

        // Almacén base para que los tests de edición funcionen
        findUseCase.almacenARetornar = new Almacen.Builder()
                .setId(10)
                .setNombre("Almacen de Prueba")
                .build();

        presenter = new ModifyAlmacenPresenter(
                10, modifyUseCase, findUseCase, () -> saveSuccessCalled = true
        );
    }

    @Test
    @DisplayName("CP-24: Carga de datos al abrir el formulario de modificación")
    void setView_CargaDatosCorrectamente() {
        presenter.setView(view);

        assertTrue(view.formularioInicializado,
                "La vista debería rellenar el campo de nombre automáticamente al iniciar.");
        assertFalse(view.errorMostrado(),
                "No debe mostrar error si el almacén existe en el repositorio.");
    }

    @Test
    @DisplayName("CP-25: Intento de modificar almacén inexistente")
    void setView_AlmacenNoEncontrado() {
        findUseCase.almacenARetornar = null; // Simulamos que el ID ya no existe

        presenter.setView(view);

        assertTrue(view.errorMostrado(),
                "Debe notificar al usuario si el almacén no se pudo recuperar.");
    }

    @Test
    @DisplayName("CP-26: Edición con nombre menor a 10 caracteres")
    void modificar_NombreCorto() {
        presenter.setView(view);
        presenter.onModifyAlmacen("Bodega A");

        assertTrue(view.errorMostrado(),
                "El presentador debe validar el mínimo de caracteres antes de enviar al UseCase.");
        assertFalse(modifyUseCase.executeCalled,
                "No se debe intentar persistir datos que no cumplen los requisitos.");
    }

    @Test
    @DisplayName("CP-27: Edición con caracteres especiales no permitidos")
    void modificar_NombreInvalido() {
        presenter.setView(view);
        presenter.onModifyAlmacen("Almacen #Central_1");

        assertTrue(view.errorMostrado(),
                "Debe rechazarse el nombre si contiene caracteres fuera de la Regex alfanumérica.");
    }

    @Test
    @DisplayName("CP-28: Error cuando la persistencia falla (Repositorio)")
    void modificar_FalloEnBaseDatos() {
        presenter.setView(view);
        modifyUseCase.resultToReturn = false; // El repo no pudo hacer el UPDATE

        presenter.onModifyAlmacen("Almacen Principal Hermosillo");

        assertTrue(view.errorMostrado(),
                "La vista debe mostrar un error si el UseCase informa que el guardado falló.");
        assertFalse(view.fueCerrada,
                "El formulario debe permanecer abierto para que el usuario intente de nuevo.");
    }

    @Test
    @DisplayName("CP-29: Modificación de almacén exitosa")
    void modificar_Exitoso() {
        presenter.setView(view);
        modifyUseCase.resultToReturn = true;

        presenter.onModifyAlmacen("Nuevo Nombre Almacen Valido");

        assertFalse(view.errorMostrado(),
                "No debe haber errores con un flujo de datos válido.");
        assertTrue(saveSuccessCalled,
                "Debe activarse el Runnable de refresco para actualizar la lista de almacenes.");
        assertTrue(view.fueCerrada,
                "La ventana debe cerrarse al completar la operación con éxito.");
    }
}