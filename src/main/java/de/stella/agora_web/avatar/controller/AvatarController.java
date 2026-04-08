package de.stella.agora_web.avatar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.avatar.controller.dto.AvatarDTO;
import de.stella.agora_web.avatar.dto.AvatarSelectorDTO;
import de.stella.agora_web.avatar.service.IAvatarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/any/avatars")
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

    @GetMapping("/selector")
    public ResponseEntity<List<AvatarSelectorDTO>> getAvatarsForSelector() {
        return ResponseEntity.ok(avatarService.getPreloadedAvatarsForSelector());
    }

    @GetMapping("/default")
    public ResponseEntity<AvatarDTO> getDefaultAvatar() {
        return ResponseEntity.ok(avatarService.getDefaultAvatar());
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getAvatarImage(
            @PathVariable Long id,
            @RequestParam(value = "token", required = false) String token,
            HttpServletRequest request) {

        // Aquí puedes agregar validación adicional del token si es necesario
        // O confiar en que Spring Security ya validó la sesión
        AvatarDTO avatar = avatarService.getAvatarById(id);
        if (avatar == null || avatar.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        // Solo permitir acceso a avatares personalizados (no precargados)
        if (avatar.isPreloaded()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Podría ser dinámico basado en el tipo
                .header("Cache-Control", "private, max-age=3600") // Cache privado por 1 hora
                .body(avatar.getImageData());
    }

    @PostMapping
    public ResponseEntity<AvatarDTO> createAvatar(@RequestBody AvatarDTO avatarDTO) {
        return ResponseEntity.ok(avatarService.saveAvatar(avatarDTO));
    }

    @PostMapping("/upload")
    public ResponseEntity<AvatarDTO> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "displayName", required = false) String displayName) {

        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                fileName = "custom_avatar_" + System.currentTimeMillis();
            }

            String finalDisplayName = displayName != null && !displayName.isEmpty()
                    ? displayName
                    : "Custom Avatar";

            AvatarDTO savedAvatar = avatarService.uploadCustomAvatar(
                    fileName,
                    file.getBytes(),
                    finalDisplayName
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAvatar);
        } catch (java.io.IOException _) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        try {
            avatarService.deleteAvatar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException _) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
