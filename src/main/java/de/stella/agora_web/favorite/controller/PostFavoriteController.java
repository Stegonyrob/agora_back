package de.stella.agora_web.favorite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.favorite.controller.dto.PostFavoriteDTO;
import de.stella.agora_web.favorite.model.Favorite;
import de.stella.agora_web.favorite.service.impl.PostFavoriteServiceImpl;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;

@RestController
@RequestMapping(path = "${api-endpoint}/any/")
public class PostFavoriteController {

    @Autowired
    private PostFavoriteServiceImpl favoriteService;

    @GetMapping(path = "/favorites")

    public List<PostFavoriteDTO> getAllFavorites() {
        return favoriteService.getAllFavorites();
    }

    @PostMapping("/favorites/{postId}")
    public ResponseEntity<PostFavoriteDTO> createFavorite(@PathVariable Long postId, @RequestBody Long profileId) {
        PostFavoriteDTO favoriteDTO = new PostFavoriteDTO();
        favoriteDTO.setId(postId);
        favoriteDTO.setId(profileId);
        try {
            PostFavoriteDTO createdFavorite = favoriteService.createFavorite(favoriteDTO);
            if (createdFavorite != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdFavorite);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favorites/{id}")
    public PostFavoriteDTO getFavorite(@PathVariable Long id) {
        return favoriteService.getFavorite(id);
    }

    @PutMapping("/favorites/{id}")
    public ResponseEntity<PostFavoriteDTO> updateFavorite(@PathVariable Long id, @RequestBody Favorite favorite) {
        if (favorite == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            PostFavoriteDTO favoriteDTO = PostFavoriteDTO.builder().id(favorite.getId())
                    .profile(ProfileDTO.builder().id(favorite.getProfile().getId()).build())
                    .post(PostDTO.builder().id(favorite.getPost().getId()).build()).build();
            PostFavoriteDTO updatedFavorite = favoriteService.updateFavorite(id, favoriteDTO);
            if (updatedFavorite != null) {
                return ResponseEntity.ok(updatedFavorite);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favorites/{id}")
    public void deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
    }
}
