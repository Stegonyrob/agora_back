package de.stella.agora_web.posts.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.stella.agora_web.image.module.PostImage;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;

@SpringBootTest
class PostServiceImplImageIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void postShouldContainImages() {
        // Cambia el ID por uno que sepas que tiene imágenes en tu base de datos
        Long postId = 1L;
        Optional<Post> postOpt = postRepository.findById(postId);
        assertTrue(postOpt.isPresent(), "El post debe existir");
        Post post = postOpt.get();
        assertNotNull(post.getImages(), "La lista de imágenes no debe ser nula");
        assertFalse(post.getImages().isEmpty(), "El post debe tener imágenes asociadas");
        for (PostImage img : post.getImages()) {
            System.out.println("Imagen: " + img.getImageName());
        }
    }
}
