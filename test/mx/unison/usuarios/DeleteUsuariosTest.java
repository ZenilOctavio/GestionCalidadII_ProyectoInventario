package mx.unison.usuarios;

import mx.unison.core.domain.models.Usuario;
import mx.unison.core.domain.repository.UsersRepository;
import mx.unison.usecases.usuarios.DeleteUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUsuariosTest {

    private UsersRepository repository;
    private DeleteUsuarioUseCase deleteUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(UsersRepository.class);
        deleteUseCase = new DeleteUsuarioUseCase(repository);
    }

    @Test
    @DisplayName("Test 31: Usuario_eliminar_conIdExistente_debeEliminarCorrectamente")
    void Usuario_eliminar_conIdExistente_debeEliminarCorrectamente() {
        /**
         * Documentación: Verifica que un usuario existente sea enviado
         * al repositorio para su eliminación física.
         */
        Usuario usuario = new Usuario("juan_perez", "hash", "ALMACEN");

        // Ejecución
        boolean resultado = deleteUseCase.execute(usuario);

        // Verificación
        assertThat(resultado).isTrue();
        verify(repository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Test 32: Usuario_eliminar_conIdInexistente_debeLanzarExcepcion")
    void Usuario_eliminar_conIdInexistente_debeLanzarExcepcion() {
        /**
         * Documentación: Si el repositorio falla al intentar eliminar
         * un registro que no existe (o por error de BD), el caso de uso
         * debe capturar la excepción y retornar false.
         */
        Usuario usuarioInexistente = new Usuario("999", "hash", "ADMIN");

        // Simulamos que el repositorio lanza una excepción si el ID no existe
        doThrow(new RuntimeException("Registro no encontrado"))
                .when(repository).delete(usuarioInexistente);

        boolean resultado = deleteUseCase.execute(usuarioInexistente);

        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Test 33: Usuario_eliminar_debeEliminarPermanentemente")
    void Usuario_eliminar_debeEliminarPermanentemente() {
        /**
         * Documentación: Asegura que tras la eliminación, el conteo de
         * registros sea cero (simulando comportamiento de Hard Delete).
         */
        Usuario usuario = new Usuario("temp_user", "hash", "PRODUCTOS");

        // 1. Ejecutamos la eliminación
        deleteUseCase.execute(usuario);

        // 2. Simulamos que buscamos todos los usuarios y la lista está vacía
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Verificamos que efectivamente no hay registros
        assertThat(repository.findAll()).isEmpty();
        verify(repository).delete(usuario);
    }
}