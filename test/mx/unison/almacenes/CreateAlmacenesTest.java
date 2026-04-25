package mx.unison.almacenes;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.usecases.almacenes.CreateAlmacenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateAlmacenesTest {

    private AlmacenesRepository repository;
    private CreateAlmacenUseCase createUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(AlmacenesRepository.class);
        createUseCase = new CreateAlmacenUseCase(repository);
    }

    @Test
    @DisplayName("Test 34: Almacen_crear_conDatosValidos_debeCrearCorrectamente")
    void Almacen_crear_conDatosValidos_debeCrearCorrectamente() {
        String nombre = "Almacén Central";
        when(repository.createAlmacen(any(Almacen.class))).thenReturn(true);

        boolean resultado = createUseCase.execute(nombre);

        assertThat(resultado).isTrue();
        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).createAlmacen(captor.capture());

        Almacen guardado = captor.getValue();
        assertThat(guardado.getNombre()).isEqualTo(nombre);
        assertThat(guardado.getFechaHoraCreacion()).isNotNull();
        assertThat(guardado.getUltimoUsuario()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Test 35: Almacen_crear_conNombreNulo_debeLanzarExcepcion")
    void Almacen_crear_conNombreNulo_debeLanzarExcepcion() {
        // Se recomienda agregar validación en el UseCase: if (nombre == null) throw ...
        assertThatThrownBy(() -> {
            createUseCase.execute(null);
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Test 36: Almacen_crear_conNombreVacio_debeLanzarExcepcion")
    void Almacen_crear_conNombreVacio_debeLanzarExcepcion() {
        assertThatThrownBy(() -> {
            createUseCase.execute("");
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Test 37: Almacen_crear_conUbicacionNula_debePermitirlo")
    void Almacen_crear_conUbicacionNula_debePermitirlo() {
        /** * Nota: Tu modelo actual no tiene 'ubicacion'.
         * Si lo agregas, el test verificaría que el campo sea opcional.
         */
        boolean resultado = createUseCase.execute("Almacén Sin Dirección");
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Test 38: Almacen_crear_debeAsignarFechaHoraCreacion")
    void Almacen_crear_debeAsignarFechaHoraCreacion() {
        createUseCase.execute("Test Fecha");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).createAlmacen(captor.capture());

        // Verifica que se haya asignado un timestamp (System.currentTimeMillis() como String)
        assertThat(captor.getValue().getFechaHoraCreacion()).isNotEmpty();
        long ts = Long.parseLong(captor.getValue().getFechaHoraCreacion());
        assertThat(ts).isCloseTo(System.currentTimeMillis(), within(1000L));
    }

    @Test
    @DisplayName("Test 39: Almacen_crear_debeAsignarIdAutoincrementado")
    void Almacen_crear_debeAsignarIdAutoincrementado() {
        /**
         * En ORMLite, el ID se genera al insertar.
         * Aquí probamos que el UseCase no intente forzar un ID manual.
         */
        createUseCase.execute("Almacen 1");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).createAlmacen(captor.capture());

        // El ID debe ser 0 antes de entrar a la BD si es autoincremental
        assertThat(captor.getValue().getId()).isEqualTo(0);
    }

    @Test
    @DisplayName("Test 40: Almacen_crear_conCaracteresEspeciales_debeManejarlo")
    void Almacen_crear_conCaracteresEspeciales_debeManejarlo() {
        String nombreEspecial = "Almacén #1 - Sección A (Ñ)";
        createUseCase.execute(nombreEspecial);

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).createAlmacen(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo(nombreEspecial);
    }

    @Test
    @DisplayName("Test 41: Almacen_crear_conComillasEnNombre_noDebePermitirSQLInjection")
    void Almacen_crear_conComillasEnNombre_noDebePermitirSQLInjection() {
        String malicioso = "Almacén'; DROP TABLE almacenes; --";
        createUseCase.execute(malicioso);

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).createAlmacen(captor.capture());

        // El nombre debe guardarse literal; ORMLite se encarga del escape por nosotros
        assertThat(captor.getValue().getNombre()).isEqualTo(malicioso);
    }

    @Test
    @DisplayName("Test 42: Almacen_crear_debeGuardarUltimoUsuarioModificador")
    void Almacen_crear_debeGuardarUltimoUsuarioModificador() {
        createUseCase.execute("Cualquiera");

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(repository).createAlmacen(captor.capture());

        // En tu código actual está hardcodeado como "ADMIN"
        assertThat(captor.getValue().getUltimoUsuario()).isEqualTo("ADMIN");
    }
}
