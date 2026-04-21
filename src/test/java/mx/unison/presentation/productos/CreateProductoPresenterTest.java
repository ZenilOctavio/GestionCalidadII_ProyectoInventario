package mx.unison.presentation.productos;

import mx.unison.mocks.productos.CreateProductoUseCaseMock;
import mx.unison.mocks.productos.CreateProductoViewMock;
import mx.unison.mocks.almacenes.GetAllAlmacenesUseCaseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateProductoPresenterTest {
    private CreateProductoPresenter presenter;
    private CreateProductoViewMock view;
    private CreateProductoUseCaseMock useCase;
    private GetAllAlmacenesUseCaseMock getAllAlmacenesUseCaseMock;

    @BeforeEach
    void setUp() {
        view = new CreateProductoViewMock();
        useCase = new CreateProductoUseCaseMock();
        getAllAlmacenesUseCaseMock = new GetAllAlmacenesUseCaseMock();
        // El segundo parámetro es el Runnable de refresco (callback)
        presenter = new CreateProductoPresenter(useCase, getAllAlmacenesUseCaseMock, () -> {});
        presenter.setView(view);
    }

    @Test
    @DisplayName("CP-06: Crear producto con nombre vacío")
    void crear_NombreVacio() {
        presenter.onSaveProducto("", "100.0", "10", "Descripción válida", 1);

        assertTrue(view.llamadasError > 0,
                "El sistema debe validar que el nombre no sea una cadena vacía.");
    }

    @Test
    @DisplayName("CP-07: Crear producto con nombre menor a 10 caracteres")
    void crear_NombreCorto() {
        presenter.onSaveProducto("Laptop", "1500.0", "5", "Desc",1);

        assertTrue(view.llamadasError > 0,
                "Debería fallar si el nombre tiene menos de 10 caracteres.");
    }

    @Test
    @DisplayName("CP-08: Crear producto con nombre mayor a 64 caracteres")
    void crear_NombreLargo() {
        String nombreExcedido = "A".repeat(65);
        presenter.onSaveProducto(nombreExcedido, "10.0", "1", "Desc",1);

        assertTrue(view.llamadasError > 0,
                "El nombre no debe superar el límite de 64 caracteres definido en los requisitos.");
    }

    @Test
    @DisplayName("CP-09: Crear producto con caracteres no alfanuméricos")
    void crear_NombreInvalidoRegex() {
        presenter.onSaveProducto("Producto #!?", "50.0", "5", "Descripción",1);

        assertTrue(view.llamadasError > 0,
                "La validación por Regex debe rechazar caracteres especiales en el nombre.");
        assertFalse(useCase.saveCalled,
                "El Caso de Uso no debe ejecutarse si la validación de formato falla.");
    }

    @Test
    @DisplayName("CP-10: Crear producto con precio inválido (No numérico)")
    void crear_PrecioInvalido() {
        presenter.onSaveProducto("Nombre Valido Producto", "abc", "10", "Desc",1);

        assertTrue(view.llamadasError > 0,
                "El sistema debe capturar el error cuando el precio no es un número válido.");
    }

    @Test
    @DisplayName("CP-11: Crear producto exitosamente")
    void crear_Exitoso() {
        presenter.onSaveProducto("Laptop Gamer Pro x15", "25000.0", "10", "Descripción de prueba",1);

        assertEquals(0, view.llamadasError,
                "No debería haber errores en la vista con datos de entrada válidos.");
        assertTrue(useCase.saveCalled,
                "El Caso de Uso debe ser invocado para persistir el producto.");
        assertTrue(view.fueCerrada,
                "El formulario debe cerrarse automáticamente tras un guardado exitoso.");
    }
}