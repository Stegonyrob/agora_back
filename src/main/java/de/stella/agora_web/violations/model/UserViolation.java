package de.stella.agora_web.violations.model;

import java.time.LocalDateTime;

import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_violations")
public class UserViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ViolationType type; // OFFENSIVE_COMMENT, SPAM, HARASSMENT

    private String content; // Contenido que causó la violación
    private String reason;  // Razón específica de la violación

    @Column(name = "violation_date")
    private LocalDateTime violationDate;

    @Enumerated(EnumType.STRING)
    private ViolationStatus status; // ACTIVE, RESOLVED, APPEALED

    private Long relatedContentId; // ID del comentario/reply
    private String relatedContentType; // "COMMENT" o "REPLY"
}
