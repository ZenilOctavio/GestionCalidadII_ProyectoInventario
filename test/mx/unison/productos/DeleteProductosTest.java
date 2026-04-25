package mx.unison.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.usecases.productos.DeleteProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteProductosTest {

    private ProductsRepository repository;
    private DeleteProductUseCase deleteUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductsRepository.class);
        deleteUseCase = new DeleteProductUseCase(repository);
    }

    @Test
    @DisplayName("Test 86: Producto_eliminar_conIdExistente_debeEliminarCorrectamente")
    void Producto_eliminar_conIdExistente_debeEliminarCorrectamente() {
        // Preparar
        Producto productoExistente = new Producto();
        productoExistente.setId(1);
        productoExistente.setNombre("Producto a borrar");

        when(repository.findById(1)).thenReturn(Optional.of(productoExistente));
        when(repository.deleteProduct(productoExistente)).thenReturn(true);

        // Ejecutar
        boolean resultado = deleteUseCase.execute(1);

        // Verificar
        assertThat(resultado).isTrue();
        verify(repository, times(1)).deleteProduct(productoExistente);
    }

    @Test
    @DisplayName("Test 87: Producto_eliminar_conIdInexistente_debeLanzarExcepcion")
    void Producto_eliminar_conIdInexistente_debeLanzarExcepcion() {
        // Preparar: El repositorio no encuentra nada
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Verificar: El test espera que se lance una excepción si no existe
        // Nota: Si tu UseCase actual solo devuelve 'false', este test fallará hasta que cambies el 'return false' por un 'throw'
        assertThatThrownBy(() -> deleteUseCase.execute(999))
                .isInstanceOf(RuntimeException.class) // O una excepción personalizada como EntityNotFoundException
                .hasMessageContaining("no encontrado");

        verify(repository, never()).deleteProduct(any());
    }
}