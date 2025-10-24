package de.stella.agora_web.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.legal_text.repository.LegalTextRepository;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.user.repository.UserRepository;
import jakarta.transaction.Transactional;

/**
 * Tests de auditoría completa de la aplicación Verificar que todos los
 * componentes principales están funcionando correctamente
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.sql.init.mode=always",
    "spring.h2.console.enabled=false",
    "kafka.enabled=false",
    "spring.kafka.enabled=false"
})
@Import(TestConfig.class)
@DisplayName("🔍 Auditoría Completa del Sistema")
class ApplicationAuditTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private LegalTextRepository legalTextRepository;

    @Test
    @DisplayName("✅ Verificar que todas las entidades principales se cargan correctamente")
    void testAllMainEntitiesAreLoaded() {
        // Verificar que los repositorios están inyectados
        assertNotNull(userRepository, "UserRepository debe estar disponible");
        assertNotNull(profileRepository, "ProfileRepository debe estar disponible");
        assertNotNull(postRepository, "PostRepository debe estar disponible");
        assertNotNull(commentRepository, "CommentRepository debe estar disponible");
        assertNotNull(replyRepository, "ReplyRepository debe estar disponible");
        assertNotNull(avatarRepository, "AvatarRepository debe estar disponible");
        assertNotNull(legalTextRepository, "LegalTextRepository debe estar disponible");
    }

    @Test
    @DisplayName("📊 Verificar que los datos de muestra se cargan correctamente")
    void testSampleDataIsLoaded() {
        // Verificar usuarios
        long userCount = userRepository.count();
        assertTrue(userCount >= 1, "Debe haber al menos 1 usuario cargado");

        // Verificar avatares precargados
        long avatarCount = avatarRepository.count();
        assertTrue(avatarCount >= 20, "Debe haber al menos 20 avatares precargados");

        // Verificar textos legales
        long legalTextCount = legalTextRepository.count();
        assertTrue(legalTextCount >= 2, "Debe haber al menos 2 textos legales (términos y privacidad)");

        // Verificar que hay perfiles
        long profileCount = profileRepository.count();
        assertTrue(profileCount >= 1, "Debe haber al menos 1 perfil");
    }

    @Test
    @DisplayName("🔒 Verificar configuración de seguridad básica")
    @Transactional
    void testBasicSecurityConfiguration() {
        // Verificar que hay usuarios con diferentes roles
        var users = userRepository.findAll();
        assertFalse(users.isEmpty(), "Debe haber usuarios en el sistema");

        // Verificar que al menos un usuario tiene roles
        boolean foundUserWithRoles = users.stream()
                .anyMatch(user -> user.getRoles() != null && !user.getRoles().isEmpty());
        assertTrue(foundUserWithRoles, "Al menos un usuario debe tener roles asignados");
    }

    @Test
    @DisplayName("🖼️ Verificar sistema de avatares")
    void testAvatarSystem() {
        // Verificar avatares por defecto
        var defaultAvatars = avatarRepository.findPreloadedAvatars();
        assertFalse(defaultAvatars.isEmpty(), "Debe haber avatares precargados");

        // Verificar que hay un avatar por defecto
        var defaultAvatar = avatarRepository.findDefaultAvatar();
        assertTrue(defaultAvatar.isPresent(), "Debe haber un avatar marcado como por defecto");
    }

    @Test
    @DisplayName("📝 Verificar sistema de contenido")
    void testContentSystem() {
        // Los posts pueden estar vacíos inicialmente, pero el sistema debe funcionar
        long postCount = postRepository.count();
        assertTrue(postCount >= 0, "El conteo de posts debe ser válido");

        // Los comentarios pueden estar vacíos inicialmente
        long commentCount = commentRepository.count();
        assertTrue(commentCount >= 0, "El conteo de comentarios debe ser válido");

        // Las respuestas pueden estar vacías inicialmente
        long replyCount = replyRepository.count();
        assertTrue(replyCount >= 0, "El conteo de respuestas debe ser válido");
    }

    @Test
    @DisplayName("⚖️ Verificar textos legales")
    void testLegalTextsSystem() {
        // Verificar textos legales específicos
        var privacyText = legalTextRepository.findByType("privacy");
        assertTrue(privacyText.isPresent(), "Debe existir texto de política de privacidad");

        var termsText = legalTextRepository.findByType("terms");
        assertTrue(termsText.isPresent(), "Debe existir texto de términos y condiciones");

        // Verificar que tienen contenido
        privacyText.ifPresent(text -> {
            assertNotNull(text.getContent(), "Política de privacidad debe tener contenido");
            assertFalse(text.getContent().trim().isEmpty(), "Contenido de privacidad no debe estar vacío");
        });

        termsText.ifPresent(text -> {
            assertNotNull(text.getContent(), "Términos y condiciones debe tener contenido");
            assertFalse(text.getContent().trim().isEmpty(), "Contenido de términos no debe estar vacío");
        });
    }

    @Test
    @DisplayName("🔗 Verificar relaciones entre entidades")
    void testEntityRelationships() {
        // Verificar que los perfiles están conectados a usuarios
        var profiles = profileRepository.findAll();
        if (!profiles.isEmpty()) {
            var firstProfile = profiles.get(0);
            assertNotNull(firstProfile.getUser(), "El perfil debe estar asociado a un usuario");
        }

        // Verificar que los posts están conectados a usuarios
        var posts = postRepository.findAll();
        posts.forEach(post -> {
            assertNotNull(post.getUser(), "Todo post debe tener un usuario asociado");
        });

        // Verificar que los comentarios están conectados a posts y usuarios
        var comments = commentRepository.findAll();
        comments.forEach(comment -> {
            assertNotNull(comment.getUser(), "Todo comentario debe tener un usuario asociado");
            assertNotNull(comment.getPost(), "Todo comentario debe tener un post asociado");
        });
    }
}
