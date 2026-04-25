package mx.unison.productos;

import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.usecases.productos.FindByIdProductoUseCase;
import mx.unison.usecases.productos.GetAllProductosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReadProductosTest {

    private ProductsRepository repository;
    private FindByIdProductoUseCase findByIdUseCase;
    private GetAllProductosUseCase getAllUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductsRepository.class);
        findByIdUseCase = new FindByIdProductoUseCase(repository);
        getAllUseCase = new GetAllProductosUseCase(repository);
    }

    @Test
    @DisplayName("Test 71: Producto_obtenerPorId_conIdExistente_debeRetornarProducto")
    void Producto_obtenerPorId_conIdExistente_debeRetornarProducto() {
        Producto productoMock = new Producto();
        productoMock.setId(1);
        productoMock.setNombre("Laptop");

        when(repository.findById(1)).thenReturn(Optional.of(productoMock));

        Optional<Producto> resultado = findByIdUseCase.execute(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1);
        assertThat(resultado.get().getNombre()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("Test 72: Producto_obtenerPorId_conIdInexistente_debeRetornarNull")
    void Producto_obtenerPorId_conIdInexistente_debeRetornarNull() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        Optional<Producto> resultado = findByIdUseCase.execute(999);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Test 73: Producto_obtenerTodos_debeRetornarListaCompleta")
    void Producto_obtenerTodos_debeRetornarListaCompleta() {
        List<Producto> productos = new ArrayList<>();
        for (int i = 0; i < 10; i++) productos.add(new Producto());

        when(repository.findAll()).thenReturn(productos);

        List<Producto> resultado = getAllUseCase.execute();

        assertThat(resultado).hasSize(10);
        verify(repository, times(1)).findAll();
    }


}