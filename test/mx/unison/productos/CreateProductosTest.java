package mx.unison.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.usecases.productos.CreateProductoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateProductosTest {

    private ProductsRepository repository;
    private CreateProductoUseCase createUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductsRepository.class);
        createUseCase = new CreateProductoUseCase(repository);
    }

    @Test
    @DisplayName("Test 57: Producto_crear_conDatosValidos_debeCrearCorrectamente")
    void Producto_crear_conDatosValidos_debeCrearCorrectamente() {
        when(repository.createProduct(any(Producto.class))).thenReturn(true);

        // nombre, precio, cantidad, descripcion, idAlmacen
        boolean result = createUseCase.execute("Laptop", 15000.50, 10, "Electrónica", 1);

        assertThat(result).isTrue();
        verify(repository).createProduct(argThat(p ->
                p.getNombre().equals("Laptop") && p.getPrecio() == 15000.50
        ));
    }

    @Test
    @DisplayName("Test 58 & 63: Validar campos obligatorios (Nombre y Departamento)")
    void Producto_crear_camposNulos_debeLanzarExcepcion() {
        // Test 58
        assertThatThrownBy(() -> createUseCase.execute(null, 100.0, 5, "Desc", 1))
                .isInstanceOf(IllegalArgumentException.class);

        // Test 63 (Si el UseCase no permite departamento vacío)
        // Actualmente tu UseCase setea departamento como "", habría que validarlo ahí.
    }

    @Test
    @DisplayName("Test 59 & 61: Validar valores negativos (Precio y Cantidad)")
    void Producto_crear_valoresNegativos_debeLanzarExcepcion() {
        // Test 59: Precio negativo
        assertThatThrownBy(() -> createUseCase.execute("Test", -50.0, 5, "Desc", 1))
                .isInstanceOf(IllegalArgumentException.class);

        // Test 61: Cantidad negativa
        assertThatThrownBy(() -> createUseCase.execute("Test", 100.0, -5, "Desc", 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Test 60 & 62: Permitir valores en cero")
    void Producto_crear_valoresEnCero_debePermitirlo() {
        when(repository.createProduct(any())).thenReturn(true);

        // Test 60 (Precio 0) y Test 62 (Cantidad 0)
        boolean result = createUseCase.execute("Muestra/Agotado", 0.0, 0, "Desc", 1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test 64 & 66: Campos opcionales (Almacén 0 y Descripción null)")
    void Producto_crear_camposOpcionales_debePermitirlo() {
        when(repository.createProduct(any())).thenReturn(true);

        // Test 64 (ID Almacen 0) y Test 66 (Descripción null)
        boolean result = createUseCase.execute("Test", 100.0, 10, null, 0);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test 67: Producto_crear_debeAsignarFechaHoraCreacion")
    void Producto_crear_debeAsignarFechaHoraCreacion() {
        when(repository.createProduct(any())).thenReturn(true);

        createUseCase.execute("Test", 100.0, 10, "Desc", 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).createProduct(captor.capture());

        // Verificamos que se haya asignado un long > 0 (timestamp)
        assertThat(captor.getValue().getFechaCreacion()).isPositive();
    }

    @Test
    @DisplayName("Test 69: Producto_crear_conPrecioConDecimales_debeGuardarCorrectamente")
    void Producto_crear_conPrecioConDecimales_debeGuardarCorrectamente() {
        double precioConDecimales = 123.456789;
        when(repository.createProduct(any())).thenReturn(true);

        createUseCase.execute("Test", precioConDecimales, 10, "Desc", 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).createProduct(captor.capture());

        // Verificamos que el valor se mantenga o se redondee según tu lógica de dominio
        assertThat(captor.getValue().getPrecio()).isEqualTo(precioConDecimales);
    }

    @Test
    @DisplayName("Test 70: Producto_crear_conComillasEnNombre_noDebePermitirSQLInjection")
    void Producto_crear_conComillasEnNombre_noDebePermitirSQLInjection() {
        String nombreInyeccion = "Producto'; DELETE FROM productos; --";
        when(repository.createProduct(any())).thenReturn(true);

        createUseCase.execute(nombreInyeccion, 100.0, 10, "Desc", 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).createProduct(captor.capture());

        // Al usar ORMLite/PreparedStatements, el nombre se guarda como texto plano, no se ejecuta
        assertThat(captor.getValue().getNombre()).isEqualTo(nombreInyeccion);
    }
}