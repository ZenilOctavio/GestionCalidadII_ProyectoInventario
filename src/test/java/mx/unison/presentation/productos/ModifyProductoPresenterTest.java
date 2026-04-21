package mx.unison.presentation.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.mocks.productos.FindByIdUseCaseMock;
import mx.unison.mocks.productos.ModifyProductoViewMock;
import mx.unison.mocks.productos.ModifyUseCaseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModifyProductoPresenterTest {
    private ModifyProductoPresenter presenter;
    private ModifyProductoViewMock view;
    private ModifyUseCaseMock modifyUseCase;
    private FindByIdUseCaseMock findUseCase;
    private boolean saveSuccessCalled = false;

    @BeforeEach
    void setUp() {
        view = new ModifyProductoViewMock();
        modifyUseCase = new ModifyUseCaseMock();
        findUseCase = new FindByIdUseCaseMock();

        // Simulamos un producto existente por defecto para los tests de guardado
        findUseCase.productoRetornado = new Producto.Builder()
                .setId(1).setNombre("Producto Original").setPrecio(10.0)
                .setCantidad(5).setDescripcion("Desc").build();

        presenter = new ModifyProductoPresenter(
                1, modifyUseCase, findUseCase, null, () -> saveSuccessCalled = true
        );
    }

    @Test
    @DisplayName("CP-12: Inicialización exitosa del formulario")
    void setView_CargaDatosCorrectamente() {
        presenter.setView(view);

        assertTrue(view.formularioInicializado,
                "La vista debería cargar los datos del producto al asignar el Presenter.");
        assertFalse(view.errorMostrado(),
                "No debería haber errores si el producto existe en la base de datos.");
    }

    @Test
    @DisplayName("CP-13: Error al no encontrar el producto para modificar")
    void setView_ProductoNoEncontrado() {
        findUseCase.productoRetornado = null;

        presenter.setView(view);

        assertTrue(view.errorMostrado(),
                "Se debe mostrar un error si el producto a modificar ya no existe.");
        assertFalse(view.formularioInicializado,
                "No se debe intentar rellenar el formulario si no hay datos.");
    }

    @Test
    @DisplayName("CP-14: Intento de modificación con nombre vacío")
    void modificar_NombreVacio() {
        presenter.setView(view); // Primero cargamos
        presenter.onModifyProducto("", "10.0", "5", "Desc", 1);

        assertTrue(view.errorMostrado(),
                "El presentador debe impedir el guardado si el nombre se borró o está vacío.");
        assertFalse(modifyUseCase.executeCalled,
                "No se debe llamar al servicio de modificación con datos inválidos.");
    }

    @Test
    @DisplayName("CP-15: Error de formato en el precio")
    void modificar_PrecioInvalido() {
        presenter.setView(view);
        presenter.onModifyProducto("Nombre Valido", "precio_mal", "5", "Desc", 1);

        assertTrue(view.errorMostrado(),
                "Debe notificar un error de formato cuando el precio no es un número decimal.");
    }

    @Test
    @DisplayName("CP-16: Fallo en la persistencia del Repositorio")
    void modificar_ErrorEnBaseDatos() {
        presenter.setView(view);
        modifyUseCase.resultToReturn = false; // Simulamos fallo en SQLite

        presenter.onModifyProducto("Nombre Valido", "10.0", "5", "Desc", 1);

        assertTrue(view.errorMostrado(),
                "Si el repositorio falla al actualizar, la vista debe informar al usuario.");
        assertFalse(view.fueCerrada,
                "La ventana no debe cerrarse si la operación de guardado falló.");
    }

    @Test
    @DisplayName("CP-17: Modificación exitosa")
    void modificar_Exitoso() {
        presenter.setView(view);
        modifyUseCase.resultToReturn = true;

        presenter.onModifyProducto("Nombre Editado", "150.0", "20", "Nueva Desc", 1);

        assertFalse(view.errorMostrado(),
                "No debe haber errores en la interfaz ante un flujo de datos correcto.");
        assertTrue(saveSuccessCalled,
                "Se debe ejecutar el callback 'onSaveSuccess' para refrescar la tabla principal.");
        assertTrue(view.fueCerrada,
                "El diálogo debe cerrarse automáticamente tras confirmar los cambios.");
    }
}