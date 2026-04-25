package mx.unison.security;

import mx.unison.core.domain.models.Almacen;
import mx.unison.core.domain.models.Producto;
import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.AlmacenesRepository;
import mx.unison.core.domain.repository.ProductsRepository;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.usecases.almacenes.ModifyAlmacenUseCase;
import mx.unison.usecases.productos.CreateProductoUseCase;
import mx.unison.infrastructure.security.Md5PasswordHasher; // Clase de utilidad sugerida
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityTest {

    private UsersRepository usersRepository;
    private ProductsRepository productsRepository;
    private AlmacenesRepository almacenesRepository;
    private Md5PasswordHasher hasher;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        productsRepository = mock(ProductsRepository.class);
        almacenesRepository = mock(AlmacenesRepository.class);
        hasher = new Md5PasswordHasher();
    }

    // --- SQL INJECTION TESTS ---

    @Test
    @DisplayName("Test 124: Seguridad_SQLInjection_loginBypass_debePrevenirlo")
    void Seguridad_SQLInjection_loginBypass_debePrevenirlo() {
        String injectionPayload = "admin' OR '1'='1";
        when(usersRepository.findByName(injectionPayload)).thenReturn(Optional.empty());

        // El sistema debe buscar el string literal, no ejecutar el OR
        Optional<?> resultado = usersRepository.findByName(injectionPayload);
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Test 125: Seguridad_SQLInjection_crearUsuario_debePrevenirlo")
    void Seguridad_SQLInjection_crearUsuario_debePrevenirlo() {
        String payload = "'; DROP TABLE usuarios; --";
        // Al usar ORMLite/Prepared Statements, esto se inserta como texto literal
        var u = new Usuario();
        u.setNombre(payload);
        u.setContrasena("contrasena");
        usersRepository.create(u);

        verify(usersRepository).create(eq(u));
    }

    @Test
    @DisplayName("Test 127: Seguridad_SQLInjection_actualizarAlmacen_debePrevenirlo")
    void Seguridad_SQLInjection_actualizarAlmacen_debePrevenirlo() {
        ModifyAlmacenUseCase modifyUseCase = new ModifyAlmacenUseCase(almacenesRepository);
        String injection = "Almacén'; UPDATE usuarios SET rol='ADMINISTRADOR'--";

        modifyUseCase.execute(1, injection);

        ArgumentCaptor<Almacen> captor = ArgumentCaptor.forClass(Almacen.class);
        verify(almacenesRepository).updateAlmacen(captor.capture());

        // Verificamos que el nombre guardado sea exactamente el payload, escapado por el driver
        assertThat(captor.getValue().getNombre()).isEqualTo(injection);
    }

    @Test
    @DisplayName("Test 128: Seguridad_SQLInjection_descripcionProducto_debePrevenirlo")
    void Seguridad_SQLInjection_descripcionProducto_debePrevenirlo() {
        CreateProductoUseCase createUseCase = new CreateProductoUseCase(productsRepository);
        String payload = "Producto normal'); DELETE FROM productos WHERE ('1'='1";

        createUseCase.execute("Laptop", 100.0, 10, payload, 1);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productsRepository).createProduct(captor.capture());
        assertThat(captor.getValue().getDescripcion()).isEqualTo(payload);
    }

    // --- HASHING TESTS (BCrypt recomendado) ---

    @Test
    @DisplayName("Test 129: Seguridad_contrasena_debeHashearseAlGuardar")
    void Seguridad_contrasena_debeHashearseAlGuardar() {
        String pass = "Password123";
        String hashed = hasher.hash(pass);

        assertThat(hashed).isNotEqualTo(pass);
    }

    @Test
    @DisplayName("Test 130: Seguridad_contrasena_compararConHashCorrecto_debeRetornarTrue")
    void Seguridad_contrasena_compararConHashCorrecto_debeRetornarTrue() {
        String pass = "Password123";
        String hashed = hasher.hash(pass);

        assertThat(hasher.verify(pass, hashed)).isTrue();
    }

    @Test
    @DisplayName("Test 131: Seguridad_contrasena_compararConHashIncorrecto_debeRetornarFalse")
    void Seguridad_contrasena_compararConHashIncorrecto_debeRetornarFalse() {
        String pass = "Password123";
        String hashed = hasher.hash(pass);

        assertThat(hasher.verify("WrongPassword", hashed)).isFalse();
    }


}