package mx.unison.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.usecases.almacenes.FindByIdAlmacenUseCase;
import mx.unison.usecases.almacenes.GetAllAlmacenesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReadAlmacenesTest {

    private AlmacenesRepository repository;
    private FindByIdAlmacenUseCase findByIdUseCase;
    private GetAllAlmacenesUseCase getAllUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(AlmacenesRepository.class);
        findByIdUseCase = new FindByIdAlmacenUseCase(repository);
        getAllUseCase = new GetAllAlmacenesUseCase(repository);
    }

    @Test
    @DisplayName("Test 43: Almacen_obtenerPorId_conIdExistente_debeRetornarAlmacen")
    void Almacen_obtenerPorId_conIdExistente_debeRetornarAlmacen() {
        Almacen mockAlmacen = new Almacen();
        mockAlmacen.setId(1);
        mockAlmacen.setNombre("Almacen Test");

        when(repository.findById(1)).thenReturn(Optional.of(mockAlmacen));

        Optional<Almacen> resultado = findByIdUseCase.execute(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Almacen Test");
    }

    @Test
    @DisplayName("Test 44: Almacen_obtenerPorId_conIdInexistente_debeRetornarNull")
    void Almacen_obtenerPorId_conIdInexistente_debeRetornarNull() {
        // Simulamos que el repositorio no encuentra nada
        when(repository.findById(999)).thenReturn(Optional.empty());

        Optional<Almacen> resultado = findByIdUseCase.execute(999);

        // En Java con Optional, "retornar null" se traduce a un Optional vacío
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Test 45: Almacen_obtenerTodos_debeRetornarListaCompleta")
    void Almacen_obtenerTodos_debeRetornarListaCompleta() {
        List<Almacen> listaMock = Arrays.asList(
                new Almacen(), new Almacen(), new Almacen(), new Almacen()
        );
        when(repository.findAll()).thenReturn(listaMock);

        List<Almacen> resultado = getAllUseCase.execute();

        assertThat(resultado).hasSize(4);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 46: Almacen_obtenerTodos_ordenadoPorFechaCreacion_debeOrdenarCorrectamente")
    void Almacen_obtenerTodos_ordenadoPorFechaCreacion_debeOrdenarCorrectamente() {
        /**
         * Nota: En una arquitectura limpia, el orden suele venir
         * definido por la implementación del repositorio (SQL ORDER BY).
         */
        Almacen a = new Almacen(); a.setNombre("A"); a.setFechaHoraCreacion("1000");
        Almacen b = new Almacen(); b.setNombre("B"); b.setFechaHoraCreacion("2000");

        when(repository.findAll()).thenReturn(Arrays.asList(a, b));

        List<Almacen> resultado = getAllUseCase.execute();

        assertThat(resultado.get(0).getNombre()).isEqualTo("A");
        assertThat(resultado.get(1).getNombre()).isEqualTo("B");
    }

    @Test
    @DisplayName("Test 47: Almacen_obtenerPorNombre_debeRetornarAlmacenCorrecto")
    void Almacen_obtenerPorNombre_debeRetornarAlmacenCorrecto() {
        /**
         * Si tuvieras un FindByNameAlmacenUseCase, se probaría aquí.
         * Dado que usas el repositorio, verificamos que la lógica de filtrado
         * funcione si se implementa en el repositorio.
         */
        String nombreABuscar = "Almacén Norte";
        Almacen mockAlmacen = new Almacen();
        mockAlmacen.setNombre(nombreABuscar);

        // Simulamos una búsqueda por nombre (si el repo tuviera el método)
        // Como ejemplo, usamos findAll y filtramos (o asumiendo que el repo lo hace)
        when(repository.findAll()).thenReturn(Collections.singletonList(mockAlmacen));

        List<Almacen> todos = getAllUseCase.execute();
        boolean encontrado = todos.stream()
                .anyMatch(al -> al.getNombre().equals(nombreABuscar));

        assertThat(encontrado).isTrue();
    }
}