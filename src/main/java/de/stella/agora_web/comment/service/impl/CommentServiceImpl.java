package de.stella.agora_web.comment.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.exceptions.CommentNotFoundException;
import de.stella.agora_web.comment.kafka.component.producer.CommentKafkaProducer;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.comment.service.ICommentService;
import de.stella.agora_web.comment.service.IMessageQueueService;
import de.stella.agora_web.moderation.model.ModeratableContent;
import de.stella.agora_web.moderation.service.IModerationService;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@Service
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final IMessageQueueService messageQueue;
    @SuppressWarnings("unused")
    private final CommentKafkaProducer kafkaProducer;
    private final IModerationService moderationService;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
            UserRepository userRepository, IMessageQueueService messageQueue,
            CommentKafkaProducer kafkaProducer, IModerationService moderationService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.messageQueue = messageQueue;
        this.kafkaProducer = kafkaProducer;
        this.moderationService = moderationService;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment createComment(CommentDTO commentDTO, User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuario autenticado requerido");
        } else {
            Comment newComment = new Comment();

            // Asignar post
            Post post = postRepository.findById(commentDTO.getPostId()).orElse(null);
            newComment.setPost(post);

            // Asignar usuario autenticado (siempre desde la base de datos)
            User dbUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            newComment.setUser(dbUser);

            // Setear título, mensaje y fecha
            newComment.setMessage(commentDTO.getMessage());
            newComment.setCreationDate(LocalDateTime.now());

            // Inicializar archived a false
            newComment.setArchived(false);

            // ✅ MODERACIÓN: Verificar contenido inapropiado ANTES de guardar
            ModeratableContent moderatable = newComment;
            var censuredComment = moderationService.moderateComment(moderatable);
            if (censuredComment != null) {
                // El comentario fue censured por contenido inapropiado
                throw new IllegalArgumentException("Comentario rechazado por contener contenido inapropiado");
            }

            // Guardar el comentario
            commentRepository.save(newComment);

            // Notificación Kafka - Siempre disponible (dummy si kafka está deshabilitado)
            CommentNotificationDTO notification = new CommentNotificationDTO();
            notification.setCommentId(newComment.getId());
            notification.setAuthor(user.getUsername());
            notification.setMessage(newComment.getMessage());
            // kafkaProducer.sendCommentNotification(notification); // Comentado temporalmente para aislar problema de bloqueo

            return newComment;
        }
    }

    @Override
    public Comment updateComment(Long id, CommentDTO commentDTO, User user) {
        Comment existingComment = commentRepository.findById(id).orElse(null);
        if (existingComment == null) {
            return null;
        }

        // Solo el dueño o admin puede editar
        if (!existingComment.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new SecurityException("No tienes permiso para editar este comentario.");
        }

        // ✅ MODERACIÓN: Verificar contenido inapropiado ANTES de actualizar
        existingComment.setMessage(commentDTO.getMessage());
        ModeratableContent moderatable = existingComment;
        var censuredComment = moderationService.moderateComment(moderatable);
        if (censuredComment != null) {
            // El comentario editado fue censurado por contenido inapropiado
            throw new IllegalArgumentException("Comentario editado rechazado por contener contenido inapropiado");
        }

        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return;
        }

        // Solo el dueño o admin puede borrar
        if (!comment.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new SecurityException("No tienes permiso para borrar este comentario.");
        }

        commentRepository.deleteById(id);
    }

    @Scheduled(fixedDelay = 1000)
    public void processMessageQueue() {
        List<Comment> comments = messageQueue.poll();
        commentRepository.saveAll(comments);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAllByOrderByCreationDateAsc();
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
    }

    @Override
    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }
}
