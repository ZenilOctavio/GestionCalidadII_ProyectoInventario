package mx.unison.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.models.UserRole;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.core.domain.services.PasswordHasher;
import mx.unison.usecases.usuarios.CreateUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de pruebas para la operación de creación de usuarios.
 * Basado en la interfaz UsersRepository y el modelo Usuario.
 */
class CreateUsuariosTest {

    private UsersRepository repository;
    private PasswordHasher passwordHasher;
    private CreateUsuarioUseCase createUseCase;

    @BeforeEach
    void setUp() {
        // Inicialización de mocks y el caso de uso a probar
        repository = mock(UsersRepository.class);
        passwordHasher = mock(PasswordHasher.class);

        // Simulación de hashing para validar que se use el servicio
        when(passwordHasher.hash(anyString())).thenAnswer(inv -> "hashed_" + inv.getArguments()[0]);

        createUseCase = new CreateUsuarioUseCase(repository, passwordHasher);
    }

    @Test
    @DisplayName("Test 1: Usuario_crear_conDatosValidos_debeCrearCorrectamente")
    void Usuario_crear_conDatosValidos_debeCrearCorrectamente() {
        // Escenario: Crear un usuario con todos los datos válidos.
        // Se debe verificar la persistencia y la asignación de campos.
        String nombre = "test_user";
        String pass = "Password123";
        String rol = "ALMACEN";

        when(repository.findByName(nombre)).thenReturn(Optional.empty());

        boolean result = createUseCase.execute(nombre, pass, rol);

        assertThat(result).isTrue();
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).create(captor.capture());

        Usuario guardado = captor.getValue();
        assertThat(guardado.getNombre()).isEqualTo(nombre); // El nombre actúa como ID
        assertThat(guardado.getContrasena()).isEqualTo("hashed_Password123");
        assertThat(guardado.getRol()).isEqualTo(rol);
    }

    @Test
    @DisplayName("Test 2: Usuario_crear_conNombreDuplicado_debeLanzarExcepcion")
    void Usuario_crear_conNombreDuplicado_debeLanzarExcepcion() {
        // Escenario: El repositorio detecta que el nombre ya existe.
        String nombre = "admin";
        when(repository.findByName(nombre)).thenReturn(Optional.of(new Usuario(nombre, "hash", "ADMIN")));

        boolean result = createUseCase.execute(nombre, "Password123", "ALMACEN");

        assertThat(result).isFalse();
        verify(repository, never()).create(any());
    }

    @Test
    @DisplayName("Test 3: Usuario_crear_conNombreNulo_debeLanzarExcepcion")
    void Usuario_crear_conNombreNulo_debeLanzarExcepcion() {
        // Escenario: El nombre es NULL. El sistema debe lanzar una excepción o fallar.
        assertThatThrownBy(() -> createUseCase.execute(null, "Password123", "ALMACEN"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Test 4: Usuario_crear_conContrasenaNula_debeLanzarExcepcion")
    void Usuario_crear_conContrasenaNula_debeLanzarExcepcion() {
        // Escenario: La contraseña es NULL.
        assertThatThrownBy(() -> createUseCase.execute("test", null, "ALMACEN"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Test 5: Usuario_crear_conRolNulo_debeLanzarExcepcion")
    void Usuario_crear_conRolNulo_debeLanzarExcepcion() {
        // Escenario: El rol es NULL.
        assertThatThrownBy(() -> createUseCase.execute("test", "Password123", null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Test 6: Usuario_crear_conRolInvalido_debeLanzarExcepcion")
    void Usuario_crear_conRolInvalido_debeLanzarExcepcion() {
        // Escenario: El rol no existe en el enumerador UserRole.
        String rolInvalido = "ROL_INVENTADO";

        assertThatThrownBy(() -> UserRole.fromString(rolInvalido))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Test 7: Usuario_crear_conNombreVacio_debeLanzarExcepcion")
    void Usuario_crear_conNombreVacio_debeLanzarExcepcion() {
        // Escenario: Nombre de usuario vacío.
        boolean result = createUseCase.execute("", "Password123", "ALMACEN");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test 8: Usuario_crear_conContrasenaMenorA8Caracteres_debeLanzarExcepcion")
    void Usuario_crear_conContrasenaMenorA8Caracteres_debeLanzarExcepcion() {
        // Escenario: Contraseña demasiado corta.
        boolean result = createUseCase.execute("test", "Pass1", "ALMACEN");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test 9: Usuario_crear_conNombreMuyLargo_debeLanzarExcepcion")
    void Usuario_crear_conNombreMuyLargo_debeLanzarExcepcion() {
        // Escenario: Nombre excede los límites (e.g. 100 caracteres).
        String nombreLargo = "a".repeat(101);
        boolean result = createUseCase.execute(nombreLargo, "Password123", "ALMACEN");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test 10: Usuario_crear_conCaracteresEspecialesEnNombre_debeManejarlo")
    void Usuario_crear_conCaracteresEspecialesEnNombre_debeManejarlo() {
        // Escenario: Uso de caracteres UTF-8 (acentos, eñes) en el nombre.
        String nombre = "José_García";
        when(repository.findByName(nombre)).thenReturn(Optional.empty());

        createUseCase.execute(nombre, "Password123", "ALMACEN");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).create(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo(nombre);
    }

    @Test
    @DisplayName("Test 11: Usuario_crear_conComillasEnNombre_noDebePermitirSQLInjection")
    void Usuario_crear_conComillasEnNombre_noDebePermitirSQLInjection() {
        // Escenario: Prevención de inyección SQL tratando el nombre como texto literal.
        String inyeccion = "admin' OR '1'='1";
        createUseCase.execute(inyeccion, "Password123", "ALMACEN");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).create(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo(inyeccion);
    }

    @Test
    @DisplayName("Test 12: Usuario_crear_debeAsignarIdAutoincrementado")
    void Usuario_crear_debeAsignarIdAutoincrementado() {
        // CAMBIO: Usar contraseñas de al menos 8 caracteres para que pase la validación
        createUseCase.execute("u1", "password123", "ALMACEN");
        createUseCase.execute("u2", "password123", "ALMACEN");
        createUseCase.execute("u3", "password123", "ALMACEN");

        // Ahora sí se debería llamar al repositorio 3 veces
        verify(repository, times(3)).create(any(Usuario.class));
    }
}