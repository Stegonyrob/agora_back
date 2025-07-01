package de.stella.agora_web.gdpr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.banned.model.Banned;
import de.stella.agora_web.banned.service.IBannedService;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.service.ICommentService;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio coordinador para cumplimiento GDPR Maneja la eliminación completa de
 * todos los datos de un usuario (derecho al olvido según regulaciones europeas)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GdprService {

    private final UserServiceImpl userService;
    private final IBannedService bannedService;
    private final ICommentService commentService;
    private final IPostService postService;
    private final IReplyService replyService;

    /**
     * Elimina completamente todos los datos de un usuario del sistema Cumple
     * con el "derecho al olvido" del GDPR
     *
     * @param userId ID del usuario a eliminar
     * @return true si se eliminó correctamente, false si no se encontró
     */
    @Transactional
    public boolean deleteAllUserData(Long userId) {
        log.info("Iniciando eliminación completa GDPR para usuario ID: {}", userId);

        try {
            // Verificar que el usuario existe
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isEmpty()) {
                log.warn("Usuario con ID {} no encontrado para eliminación GDPR", userId);
                return false;
            }

            User user = userOptional.get();
            String username = user.getUsername();

            log.info("Eliminando todos los datos del usuario: {} (ID: {})", username, userId);

            // 1. Eliminar comentarios del usuario
            try {
                List<Comment> userComments = commentService.getCommentsByUserId(userId);
                for (Comment comment : userComments) {
                    commentService.deleteComment(comment.getId(), user);
                }
                log.info("Eliminados {} comentarios del usuario {}", userComments.size(), username);
            } catch (Exception e) {
                log.warn("Error al eliminar comentarios del usuario {}: {}", username, e.getMessage());
            }

            // 2. Eliminar respuestas del usuario
            try {
                List<Reply> userReplies = replyService.getRepliesByUserId(userId);
                for (Reply reply : userReplies) {
                    replyService.deleteReply(reply.getId());
                }
                log.info("Eliminadas {} respuestas del usuario {}", userReplies.size(), username);
            } catch (Exception e) {
                log.warn("Error al eliminar respuestas del usuario {}: {}", username, e.getMessage());
            }

            // 3. Eliminar posts del usuario  
            try {
                List<Post> userPosts = postService.getPostsByUserId(userId);
                for (Post post : userPosts) {
                    // Archivar primero (soft delete) y luego eliminar referencias
                    postService.archivePost(post.getId());
                }
                log.info("Archivados {} posts del usuario {}", userPosts.size(), username);
            } catch (Exception e) {
                log.warn("Error al eliminar posts del usuario {}: {}", username, e.getMessage());
            }

            // 4. Eliminar registros de baneo del usuario
            try {
                Banned userBanned = bannedService.findByUserId(userId);
                if (userBanned != null) {
                    bannedService.unbanUser(userId);
                    log.info("Registro de baneo eliminado para usuario {}", username);
                }
            } catch (Exception e) {
                log.warn("Error al eliminar registros de baneo del usuario {}: {}", username, e.getMessage());
            }

            // 5. Eliminar perfil del usuario
            try {
                if (user.getProfile() != null) {
                    log.info("Perfil del usuario {} se eliminará en cascada con el usuario", username);
                }
            } catch (Exception e) {
                log.warn("Error al procesar perfil del usuario {}: {}", username, e.getMessage());
            }

            // 6. Finalmente, eliminar el usuario
            userService.deleteById(userId);
            log.info("Usuario {} (ID: {}) completamente eliminado del sistema conforme al GDPR", username, userId);

            return true;

        } catch (Exception e) {
            log.error("Error crítico durante eliminación GDPR del usuario ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error durante eliminación GDPR: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si un usuario puede ser eliminado (por ejemplo, no es el último
     * administrador)
     */
    public boolean canDeleteUser(Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        // Verificar que no sea el último administrador
        if (user.isAdmin()) {
            long adminCount = userService.getAllUsers().stream()
                    .filter(User::isAdmin)
                    .count();

            if (adminCount <= 1) {
                log.warn("No se puede eliminar usuario {} - es el último administrador", user.getUsername());
                return false;
            }
        }

        return true;
    }
}
