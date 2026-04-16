package de.stella.agora_web.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.service.IEventLoveService;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.model.User.SanctionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api-endpoint}/events")
@RequiredArgsConstructor
public class EventLoveController {

    @PutMapping("/{eventId}/unlove-anon")
    public ResponseEntity<Void> unloveEventAnonymously(@PathVariable Long eventId) {
        try {
            eventLoveService.unloveEventAnonymously(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al quitar love anónimo al evento {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{eventId}/love-anon")
    public ResponseEntity<Void> loveEventAnonymously(@PathVariable Long eventId) {
        try {
            eventLoveService.loveEventAnonymously(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al dar love anónimo al evento {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private final IEventLoveService eventLoveService;
    private static final Logger auditLogger = LoggerFactory.getLogger("AuditLogger");

    @Autowired
    private ProfileRepository profileRepository;

    @PutMapping("/{eventId}/love")
    public ResponseEntity<Void> loveEvent(@PathVariable Long eventId, @RequestParam Long profileId) {
        var profileOpt = profileRepository.findByIdWithUser(profileId);
        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = profileOpt.get().getUser();
        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            auditLogger.warn("Intento bloqueado: usuario {} expulsado intentó dar love a evento {}", user.getId(), eventId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (user.getSanctionStatus() == SanctionStatus.SUSPENDED) {
            auditLogger.warn("Intento bloqueado: usuario {} suspendido intentó dar love a evento {}", user.getId(), eventId);
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
        try {
            eventLoveService.loveEvent(eventId, profileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al dar love al evento {} por perfil {}: {}", eventId, profileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{eventId}/unlove")
    public ResponseEntity<Void> unloveEvent(@PathVariable Long eventId, @RequestParam Long profileId) {
        var profileOpt = profileRepository.findByIdWithUser(profileId);
        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = profileOpt.get().getUser();
        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            auditLogger.warn("Intento bloqueado: usuario {} expulsado intentó quitar love a evento {}", user.getId(), eventId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (user.getSanctionStatus() == SanctionStatus.SUSPENDED) {
            auditLogger.warn("Intento bloqueado: usuario {} suspendido intentó quitar love a evento {}", user.getId(), eventId);
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
        try {
            eventLoveService.unloveEvent(eventId, profileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al quitar love al evento {} por perfil {}: {}", eventId, profileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{eventId}/love/status")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<LoveResponse> getLoveStatus(@PathVariable Long eventId, @RequestParam Long profileId) {
        try {
            boolean hasLove = eventLoveService.hasLove(eventId, profileId);
            long totalLoves = eventLoveService.countLoves(eventId);
            LoveResponse response = new LoveResponse(hasLove, totalLoves);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener estado de love para evento {} y perfil {}: {}", eventId, profileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{eventId}/loves/count")
    public ResponseEntity<Long> getLovesCount(@PathVariable Long eventId) {
        try {
            long count = eventLoveService.countLoves(eventId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error al obtener contador de loves para evento {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public static class LoveResponse {

        private boolean liked;
        private long totalLoves;

        public LoveResponse(boolean liked, long totalLoves) {
            this.liked = liked;
            this.totalLoves = totalLoves;
        }

        public boolean isLiked() {
            return liked;
        }

        public long getTotalLoves() {
            return totalLoves;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public void setTotalLoves(long totalLoves) {
            this.totalLoves = totalLoves;
        }
    }
}
