package mx.unison.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.usecases.productos.ModifyProductoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateProductosTest {

    private ProductsRepository repository;
    private ModifyProductoUseCase modifyUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductsRepository.class);
        modifyUseCase = new ModifyProductoUseCase(repository);
    }

    @Test
    @DisplayName("Test 78, 81, 82 & 83: Actualización de campos específicos")
    void Producto_actualizar_campos_debeActualizarCorrectamente() {
        when(repository.updateProduct(any(Producto.class))).thenReturn(true);

        // Ejecutar actualización (id, nombre, precio, cantidad, descripcion, idAlmacen)
        boolean result = modifyUseCase.execute(1, "Laptop Pro", 18000.0, 5, "Nueva desc", 2);

        assertThat(result).isTrue();

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).updateProduct(captor.capture());
        Producto actualizado = captor.getValue();

        assertThat(actualizado.getId()).isEqualTo(1);
        assertThat(actualizado.getNombre()).isEqualTo("Laptop Pro");
        assertThat(actualizado.getPrecio()).isEqualTo(18000.0);
        assertThat(actualizado.getCantidad()).isEqualTo(5);
        assertThat(actualizado.getAlmacenId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test 79: Producto_actualizar_debeActualizarFechaHoraUltimaModificacion")
    void Producto_actualizar_debeActualizarFechaHoraUltimaModificacion() {
        when(repository.updateProduct(any())).thenReturn(true);

        modifyUseCase.execute(1, "Test", 100.0, 10, "Desc", 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).updateProduct(captor.capture());

        // Verificamos que se haya asignado un valor a fechaModificacion (String en tu modelo)
        assertThat(captor.getValue().getFechaModificacion()).isNotNull();
        assertThat(Long.parseLong(captor.getValue().getFechaModificacion())).isPositive();
    }

    @Test
    @DisplayName("Test 80: Producto_actualizar_debeActualizarUltimoUsuarioModificador")
    void Producto_actualizar_debeActualizarUltimoUsuarioModificador() {
        when(repository.updateProduct(any())).thenReturn(true);

        modifyUseCase.execute(1, "Test", 100.0, 10, "Desc", 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).updateProduct(captor.capture());

        // Verificamos que el usuario sea el definido en el caso de uso (ej. "ADMIN")
        assertThat(captor.getValue().getUltimoUsuario()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Test 84: Producto_actualizar_noDebeModificarFechaCreacion")
    void Producto_actualizar_noDebeModificarFechaCreacion() {
        when(repository.updateProduct(any())).thenReturn(true);

        modifyUseCase.execute(1, "Test", 100.0, 10, "Desc", 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repository).updateProduct(captor.capture());

        // En una actualización, el objeto nuevo suele tener fechaCreacion en 0 o default
        // porque la BD no debe sobreescribirla si no se incluye en el UPDATE.
        // Aquí validamos que el caso de uso no le asigne un valor nuevo de timestamp.
        assertThat(captor.getValue().getFechaCreacion()).isZero();
    }

    @Test
    @DisplayName("Test 85: Producto_actualizar_conPrecioNegativo_debeLanzarExcepcion")
    void Producto_actualizar_conPrecioNegativo_debeLanzarExcepcion() {
        // Este test fallará hasta que añadas la validación en el UseCase
        assertThatThrownBy(() -> modifyUseCase.execute(1, "Test", -50.0, 10, "Desc", 1))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repository, never()).updateProduct(any());
    }
}