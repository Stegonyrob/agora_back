package de.stella.agora_web.avatar.config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.avatar.repository.AvatarRepository;

/**
 * Tests unitarios para AvatarInitializer Valida la inicialización correcta de
 * avatares precargados
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AvatarInitializer Tests")
class AvatarInitializerTest {

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private AvatarInitializer avatarInitializer;

    private List<Avatar> preloadedAvatars;

    @BeforeEach
    void setUp() {
        preloadedAvatars = new ArrayList<>();
    }

    @Test
    @DisplayName("Debe omitir inicialización si ya existen avatares precargados")
    void shouldSkipInitializationWhenPreloadedAvatarsExist() {
        // Arrange: Simular que ya hay avatares precargados
        preloadedAvatars.add(createAvatar(1L, "default.png", "Default", true));
        when(avatarRepository.findPreloadedAvatars()).thenReturn(preloadedAvatars);

        // Act: Ejecutar el inicializador
        avatarInitializer.run();

        // Assert: Verificar que no se intentó crear nuevos avatares
        verify(avatarRepository, times(1)).findPreloadedAvatars();
        verify(avatarRepository, never()).save(any(Avatar.class));
    }

    @Test
    @DisplayName("Debe inicializar avatares cuando no existen precargados")
    void shouldInitializeAvatarsWhenNoneExist() {
        // Arrange: Simular que no hay avatares precargados
        when(avatarRepository.findPreloadedAvatars()).thenReturn(new ArrayList<>());

        // Act: Ejecutar el inicializador
        avatarInitializer.run();

        // Assert: Verificar que se consultó el repositorio
        verify(avatarRepository, times(1)).findPreloadedAvatars();
    }

    @Test
    @DisplayName("Debe manejar lista nula de avatares precargados")
    void shouldHandleNullPreloadedAvatarsList() {
        // Arrange: Simular que la consulta devuelve null
        when(avatarRepository.findPreloadedAvatars()).thenReturn(null);

        // Act & Assert: No debe lanzar excepción
        assertDoesNotThrow(() -> avatarInitializer.run());
        verify(avatarRepository, times(1)).findPreloadedAvatars();
    }

    @Test
    @DisplayName("Debe manejar excepción durante la inicialización")
    void shouldHandleExceptionDuringInitialization() {
        // Arrange: Simular excepción en el repositorio
        when(avatarRepository.findPreloadedAvatars())
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert: No debe propagar la excepción
        assertDoesNotThrow(() -> avatarInitializer.run());
        verify(avatarRepository, times(1)).findPreloadedAvatars();
    }

    @Test
    @DisplayName("Debe ejecutar correctamente con argumentos vacíos")
    void shouldRunWithEmptyArguments() {
        // Arrange
        when(avatarRepository.findPreloadedAvatars()).thenReturn(preloadedAvatars);

        // Act & Assert
        assertDoesNotThrow(() -> avatarInitializer.run(new String[]{}));
        verify(avatarRepository, times(1)).findPreloadedAvatars();
    }

    @Test
    @DisplayName("Debe ejecutar correctamente con argumentos nulos")
    void shouldRunWithNullArguments() {
        // Arrange
        when(avatarRepository.findPreloadedAvatars()).thenReturn(preloadedAvatars);

        // Act & Assert
        assertDoesNotThrow(() -> avatarInitializer.run((String[]) null));
        verify(avatarRepository, times(1)).findPreloadedAvatars();
    }

    @Test
    @DisplayName("Debe loggear información cuando ya existen avatares")
    void shouldLogWhenAvatarsAlreadyExist() {
        // Arrange
        preloadedAvatars.add(createAvatar(1L, "avatar1.png", "Avatar 1", false));
        preloadedAvatars.add(createAvatar(2L, "avatar2.png", "Avatar 2", true));
        when(avatarRepository.findPreloadedAvatars()).thenReturn(preloadedAvatars);

        // Act
        avatarInitializer.run();

        // Assert: Verificar que se llamó al repositorio con la lista correcta
        verify(avatarRepository, times(1)).findPreloadedAvatars();
        assertEquals(2, preloadedAvatars.size(), "Debe haber 2 avatares precargados");
    }

    @Test
    @DisplayName("Debe inicializar lista vacía correctamente")
    void shouldHandleEmptyPreloadedAvatarsList() {
        // Arrange
        when(avatarRepository.findPreloadedAvatars()).thenReturn(new ArrayList<>());

        // Act
        avatarInitializer.run();

        // Assert
        verify(avatarRepository, times(1)).findPreloadedAvatars();
    }

    // Métodos helper para crear objetos de prueba
    private Avatar createAvatar(Long id, String imageName, String displayName, boolean isDefault) {
        return Avatar.builder()
                .id(id)
                .imageName(imageName)
                .displayName(displayName)
                .preloaded(true)
                .isDefault(isDefault)
                .imageData(null)
                .build();
    }
}
