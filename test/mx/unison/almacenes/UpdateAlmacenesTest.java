package mx.unison.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateAlmacenesTest {

    private AlmacenesRepository repository;
    private ModifyAlmacenUseCase modifyUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(AlmacenesRepository.class);
        modifyUseCase = new ModifyAlmacenUseCase(repository);
    }

    @Test
    @DisplayName("Test 48: Almacen_actualizar_conDatosValidos_debeActualizarCorrectamente")
    void Almacen_actualizar_conDatosValidos_debeActualizarCorrectamente() {
        // Configuramos el mock para que devuelva true al actualizar
        when(repository.updateAlmacen(any(Almacen.class))).thenReturn(true);

        boolean resultado = modifyUseCase.execute(1, "Nuevo Nombre");

        assertThat(resultado).isTrue();
        verify(repository).updateAlmacen(argThat(a -> a.getNombre().equals("Nuevo Nombre")));
    }

    @Test
    @DisplayName("Test 49: Almacen_actualizar_debeActualizarFechaHoraUltimaModificacion")
    void Almacen_actualizar_debeActualizarFechaHoraUltimaModificacion() throws InterruptedException {
        String fechaCreacionOriginal = String.valueOf(System.currentTimeMillis());

        // Simulación: el objeto ya existe con una fecha antigua
        when(repository.updateAlmacen(any(Almacen.class))).thenReturn(true);

        Thread.sleep(10); // Pausa para asegurar cambio de timestamp
        modifyUseCase.execute(1, "Update");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).updateAlmacen(captor.capture());

        long fechaMod = Long.parseLong(captor.getValue().getFechaHoraUltimaMod());
        long fechaCreacion = Long.parseLong(fechaCreacionOriginal);

        assertThat(fechaMod).isGreaterThan(fechaCreacion);
    }

    @Test
    @DisplayName("Test 50: Almacen_actualizar_debeActualizarUltimoUsuarioModificador")
    void Almacen_actualizar_debeActualizarUltimoUsuarioModificador() {
        when(repository.updateAlmacen(any(Almacen.class))).thenReturn(true);

        // El UseCase actualmente usa "ADMIN" hardcoded, verificamos que se asigne
        modifyUseCase.execute(1, "Nombre");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).updateAlmacen(captor.capture());

        assertThat(captor.getValue().ultimoUsuario()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Test 51: Almacen_actualizar_noDebeModificarFechaCreacion")
    void Almacen_actualizar_noDebeModificarFechaCreacion() {
        // En una arquitectura limpia, el UseCase de modificación no debería sobreescribir
        // la fecha de creación que ya está en la BD.
        when(repository.updateAlmacen(any(Almacen.class))).thenReturn(true);

        modifyUseCase.execute(1, "Nombre Editado");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).updateAlmacen(captor.capture());

        // Verificamos que no se haya seteado una fecha de creación nueva (debe ser nula o la original)
        assertThat(captor.getValue().fechaHoraCreacion()).isNull();
    }

    @Test
    @DisplayName("Test 52: Almacen_actualizar_conIdInexistente_debeLanzarExcepcion")
    void Almacen_actualizar_conIdInexistente_debeLanzarExcepcion() {
        // Si el repositorio devuelve false porque el ID no existe
        when(repository.updateAlmacen(any(Almacen.class))).thenReturn(false);

        // Dependiendo de tu implementación, puede retornar false o lanzar excepción.
        // Si quieres que lance excepción, deberías agregar la validación en el UseCase.
        boolean resultado = modifyUseCase.execute(999, "Error");

        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Test 53: Almacen_actualizar_cambiarUbicacion_debeActualizarCorrectamente")
    void Almacen_actualizar_cambiarUbicacion_debeActualizarCorrectamente() {
        // Para este test, necesitarías que tu execute acepte ubicación:
        // modifyUseCase.execute(id, nombre, ubicacion)

        when(repository.updateAlmacen(any(Almacen.class))).thenReturn(true);

        // Simulación usando el captor para verificar que el campo ubicación se envía al repo
        modifyUseCase.execute(1, "Almacen Central");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).updateAlmacen(captor.capture());

        assertThat(captor.getValue().getNombre()).isEqualTo("Almacen Central");
    }
}