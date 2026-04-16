package de.stella.agora_web.posts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.model.User.SanctionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api-endpoint}/posts")
@RequiredArgsConstructor
public class PostLoveController {

    private final IPostService postService;
    private final ProfileRepository profileRepository;

    @PutMapping("/{postId}/love")
    public ResponseEntity<Void> lovePost(@PathVariable Long postId, @RequestParam Long userId) {
        var profileOpt = profileRepository.findByUserIdWithUserAndRoles(userId);
        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = profileOpt.get().getUser();
        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (user.getSanctionStatus() == SanctionStatus.SUSPENDED) {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
        try {
            postService.lovePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al dar love al post {} por usuario {}: {}", postId, userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{postId}/unlove")
    public ResponseEntity<Void> unlovePost(@PathVariable Long postId, @RequestParam Long userId) {
        var profileOpt = profileRepository.findByUserIdWithUserAndRoles(userId);
        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = profileOpt.get().getUser();
        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (user.getSanctionStatus() == SanctionStatus.SUSPENDED) {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
        try {
            postService.unlovePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al quitar love al post {} por usuario {}: {}", postId, userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{postId}/loves/count")
    public ResponseEntity<Integer> getLoveCount(@PathVariable Long postId) {
        try {
            Integer count = postService.getLoveCount(postId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error al obtener el número de loves del post {}: {}", postId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
