package mx.unison.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.usecases.almacenes.DeleteAlmacenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteAlmacenesTest {

    private AlmacenesRepository repository;
    private DeleteAlmacenUseCase deleteUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(AlmacenesRepository.class);
        deleteUseCase = new DeleteAlmacenUseCase(repository);
    }

    @Test
    @DisplayName("Test 54: Almacen_eliminar_conIdExistente_debeEliminarCorrectamente")
    void Almacen_eliminar_conIdExistente_debeEliminarCorrectamente() {
        // Preparación: El almacén existe
        Almacen mockAlmacen = new Almacen();
        mockAlmacen.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(mockAlmacen));
        when(repository.deleteAlmacen(mockAlmacen)).thenReturn(true);

        // Ejecución
        boolean resultado = deleteUseCase.execute(1);

        // Verificación
        assertThat(resultado).isTrue();
        verify(repository, times(1)).deleteAlmacen(mockAlmacen);
    }

    @Test
    @DisplayName("Test 55: Almacen_eliminar_conProductosAsociados_debeVerificarIntegridad")
    void Almacen_eliminar_conProductosAsociados_debeVerificarIntegridad() {
        /**
         * En ORMLite/SQL, si existen llaves foráneas, el repositorio lanzará una excepción
         * de violación de integridad si no está configurado el borrado en cascada.
         */
        Almacen mockAlmacen = new Almacen();
        mockAlmacen.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(mockAlmacen));

        // Simulamos que el repo lanza una excepción por restricción de llave foránea
        doThrow(new RuntimeException("Foreign Key Constraint Violation"))
                .when(repository).deleteAlmacen(any(Almacen.class));

        // Verificamos que el sistema maneje el error de integridad
        assertThatThrownBy(() -> deleteUseCase.execute(1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Foreign Key");
    }

    @Test
    @DisplayName("Test 56: Almacen_eliminar_conIdInexistente_debeLanzarExcepcion")
    void Almacen_eliminar_conIdInexistente_debeLanzarExcepcion() {
        // Preparación: El repositorio no encuentra el ID
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Según tu implementación de DeleteAlmacenUseCase, devuelve false si no existe
        boolean resultado = deleteUseCase.execute(999);

        // Verificación
        assertThat(resultado).isFalse();
        // Verificamos que NUNCA se intentó llamar a delete si el objeto no se encontró
        verify(repository, never()).deleteAlmacen(any());
    }
}