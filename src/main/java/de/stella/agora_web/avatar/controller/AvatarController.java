package de.stella.agora_web.avatar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.avatar.controller.dto.AvatarDTO;
import de.stella.agora_web.avatar.service.IAvatarService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
public class AvatarController {

    private final IAvatarService avatarService;

    @GetMapping("/{id}")
    public ResponseEntity<AvatarDTO> getAvatar(@PathVariable Long id) {
        return ResponseEntity.ok(avatarService.getAvatarById(id));
    }

    @GetMapping("/name/{imageName}")
    public ResponseEntity<AvatarDTO> getAvatarByName(@PathVariable String imageName) {
        return ResponseEntity.ok(avatarService.getAvatarByImageName(imageName));
    }

    @GetMapping("/preloaded")
    public ResponseEntity<List<AvatarDTO>> getPreloadedAvatars() {
        return ResponseEntity.ok(avatarService.getAllPreloadedAvatars());
    }

    @PostMapping
    public ResponseEntity<AvatarDTO> uploadAvatar(@RequestBody AvatarDTO avatarDTO) {
        return ResponseEntity.ok(avatarService.saveAvatar(avatarDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        avatarService.deleteAvatar(id);
        return ResponseEntity.noContent().build();
    }
}