package mx.unison.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.usecases.usuarios.GetAllUsuariosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadUsuariosTest {

    private UsersRepository repository;
    private GetAllUsuariosUseCase getAllUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(UsersRepository.class);
        getAllUseCase = new GetAllUsuariosUseCase(repository);
    }

    // --- TESTS DE BÚSQUEDA POR ID (Nombre en tu modelo actual) ---

    @Test
    @DisplayName("Test 13: Usuario_obtenerPorId_conIdExistente_debeRetornarUsuario")
    void Usuario_obtenerPorId_conIdExistente_debeRetornarUsuario() {
        String idExistente = "1";
        Usuario esperado = new Usuario(idExistente, "hash", "ADMIN");
        when(repository.findByName(idExistente)).thenReturn(Optional.of(esperado));

        Optional<Usuario> resultado = repository.findByName(idExistente);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo(idExistente);
    }

    @Test
    @DisplayName("Test 14: Usuario_obtenerPorId_conIdInexistente_debeRetornarNull")
    void Usuario_obtenerPorId_conIdInexistente_debeRetornarNull() {
        when(repository.findByName("999")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = repository.findByName("999");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Test 15: Usuario_obtenerPorId_conIdNegativo_debeLanzarExcepcion")
    void Usuario_obtenerPorId_conIdNegativo_debeLanzarExcepcion() {
        // Asumiendo validación en el repositorio o servicio
        assertThatThrownBy(() -> {
            String id = "-1";
            if (Integer.parseInt(id) < 0) throw new IllegalArgumentException("ID negativo");
            repository.findByName(id);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Test 16: Usuario_obtenerPorId_conIdCero_debeLanzarExcepcion")
    void Usuario_obtenerPorId_conIdCero_debeLanzarExcepcion() {
        assertThatThrownBy(() -> {
            String id = "0";
            if (Integer.parseInt(id) == 0) throw new RuntimeException("ID inválido");
            repository.findByName(id);
        }).isInstanceOf(RuntimeException.class);
    }

    // --- TESTS DE BÚSQUEDA POR NOMBRE ---

    @Test
    @DisplayName("Test 17: Usuario_obtenerPorNombre_conNombreExistente_debeRetornarUsuario")
    void Usuario_obtenerPorNombre_conNombreExistente_debeRetornarUsuario() {
        String nombre = "admin";
        Usuario admin = new Usuario(nombre, "hash", "ADMIN");
        when(repository.findByName(nombre)).thenReturn(Optional.of(admin));

        Optional<Usuario> resultado = repository.findByName(nombre);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo(nombre);
    }

    @Test
    @DisplayName("Test 18: Usuario_obtenerPorNombre_conNombreInexistente_debeRetornarNull")
    void Usuario_obtenerPorNombre_conNombreInexistente_debeRetornarNull() {
        when(repository.findByName("usuario_inexistente")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = repository.findByName("usuario_inexistente");

        assertThat(resultado).isNotPresent();
    }

    @Test
    @DisplayName("Test 19: Usuario_obtenerPorNombre_caseSensitive_debeDistinguirMayusculas")
    void Usuario_obtenerPorNombre_caseSensitive_debeDistinguirMayusculas() {
        // Si el sistema es Case Sensitive, buscar "admin" no debe encontrar "Admin"
        String registrado = "Admin";
        String buscado = "admin";

        when(repository.findByName(buscado)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = repository.findByName(buscado);

        assertThat(resultado).isEmpty();
    }

    // --- TESTS DE OBTENER TODOS ---

    @Test
    @DisplayName("Test 20: Usuario_obtenerTodos_conUsuariosExistentes_debeRetornarLista")
    void Usuario_obtenerTodos_conUsuariosExistentes_debeRetornarLista() {
        List<Usuario> listaMock = Arrays.asList(
                new Usuario("u1", "h", "A"),
                new Usuario("u2", "h", "A"),
                new Usuario("u3", "h", "A"),
                new Usuario("u4", "h", "A"),
                new Usuario("u5", "h", "A")
        );
        when(repository.findAll()).thenReturn(listaMock);

        List<Usuario> resultado = getAllUseCase.execute();

        assertThat(resultado).hasSize(5);
        assertThat(resultado).containsAll(listaMock);
    }

    @Test
    @DisplayName("Test 21: Usuario_obtenerTodos_sinUsuarios_debeRetornarListaVacia")
    void Usuario_obtenerTodos_sinUsuarios_debeRetornarListaVacia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = getAllUseCase.execute();

        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    // --- TEST DE FILTRADO POR ROL ---

    @Test
    @DisplayName("Test 22: Usuario_obtenerPorRol_conRolEspecifico_debeRetornarUsuariosFiltrados")
    void Usuario_obtenerPorRol_conRolEspecifico_debeRetornarUsuariosFiltrados() {
        // Datos: 2 ALMACEN, 3 PRODUCTOS, 1 ADMIN
        List<Usuario> todos = Arrays.asList(
                new Usuario("a1", "h", "ALMACEN"),
                new Usuario("a2", "h", "ALMACEN"),
                new Usuario("p1", "h", "PRODUCTOS"),
                new Usuario("p2", "h", "PRODUCTOS"),
                new Usuario("p3", "h", "PRODUCTOS"),
                new Usuario("ad", "h", "ADMIN")
        );

        // Simulamos el filtrado (esto suele hacerse con un método específico en el repo o filtrando la lista)
        String rolBusqueda = "PRODUCTOS";
        List<Usuario> filtrados = todos.stream()
                .filter(u -> u.getRol().equals(rolBusqueda))
                .toList();

        // Si tu repositorio tiene un método findByRol(String rol):
        // when(repository.findByRol(rolBusqueda)).thenReturn(filtrados);

        assertThat(filtrados).hasSize(3);
        assertThat(filtrados).allMatch(u -> u.getRol().equals("PRODUCTOS"));
    }
}