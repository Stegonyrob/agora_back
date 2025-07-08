package de.stella.agora_web.posts.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests para verificar que los endpoints de posts devuelvan JSON optimizados
 * sin exceso de datos ni redundancia.
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("h2")
public class PostControllerJsonTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPostsJsonStructure() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Verificar estructura de paginación
        assert rootNode.has("content") : "Respuesta debe tener 'content' (paginado)";
        assert rootNode.has("pageable") : "Respuesta debe tener 'pageable'";
        assert rootNode.has("totalElements") : "Respuesta debe tener 'totalElements'";

        JsonNode content = rootNode.get("content");
        if (content.isArray() && content.size() > 0) {
            JsonNode firstPost = content.get(0);

            // Verificar estructura esperada de PostSummaryDTO
            assert firstPost.has("id") : "Post debe tener id";
            assert firstPost.has("title") : "Post debe tener title";
            assert firstPost.has("message") : "Post debe tener message";
            assert firstPost.has("creationDate") : "Post debe tener creationDate";
            assert firstPost.has("archived") : "Post debe tener archived";
            assert firstPost.has("userUsername") : "Post debe tener userUsername";

            // Verificar que NO tenga campos innecesarios
            assert !firstPost.has("user") : "Post NO debe incluir user completo";
            assert !firstPost.has("comments") : "Post NO debe incluir comments completos";
            assert !firstPost.has("replies") : "Post NO debe incluir replies completos";
            assert !firstPost.has("postLoves") : "Post NO debe incluir postLoves completos";
            assert !firstPost.has("images") : "Post NO debe incluir images completas";

            System.out.println("✅ Estructura JSON de getAllPosts es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testGetPostByIdJsonStructure() throws Exception {
        // Primero obtener la lista para conseguir un ID válido
        MvcResult listResult = mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode listNode = objectMapper.readTree(listResult.getResponse().getContentAsString());
        JsonNode content = listNode.get("content");

        if (content.isArray() && content.size() > 0) {
            Long postId = content.get(0).get("id").asLong();

            MvcResult result = mockMvc.perform(get("/api/posts/" + postId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Verificar estructura esperada de PostResponseDTO
            assert rootNode.has("id") : "Post debe tener id";
            assert rootNode.has("title") : "Post debe tener title";
            assert rootNode.has("message") : "Post debe tener message";
            assert rootNode.has("location") : "Post debe tener location";
            assert rootNode.has("creationDate") : "Post debe tener creationDate";
            assert rootNode.has("archived") : "Post debe tener archived";
            assert rootNode.has("published") : "Post debe tener published";
            assert rootNode.has("loves") : "Post debe tener loves count";
            assert rootNode.has("userUsername") : "Post debe tener userUsername";
            assert rootNode.has("userFullName") : "Post debe tener userFullName";
            assert rootNode.has("tags") : "Post debe tener tags";
            assert rootNode.has("imageUrls") : "Post debe tener imageUrls";
            assert rootNode.has("repliesCount") : "Post debe tener repliesCount";
            assert rootNode.has("commentsCount") : "Post debe tener commentsCount";

            // Verificar que NO tenga campos innecesarios
            assert !rootNode.has("user") : "Post NO debe incluir user completo";
            assert !rootNode.has("comments") : "Post NO debe incluir comments completos";
            assert !rootNode.has("replies") : "Post NO debe incluir replies completos";
            assert !rootNode.has("postLoves") : "Post NO debe incluir postLoves completos";
            assert !rootNode.has("images") : "Post NO debe incluir images completas con datos binarios";

            // Verificar estructura de tags
            JsonNode tags = rootNode.get("tags");
            if (tags.isArray() && tags.size() > 0) {
                JsonNode firstTag = tags.get(0);
                assert firstTag.has("id") : "Tag debe tener id";
                assert firstTag.has("name") : "Tag debe tener name";
                assert !firstTag.has("posts") : "Tag NO debe incluir posts";
                assert !firstTag.has("events") : "Tag NO debe incluir events";
            }

            // Verificar que imageUrls sea array de strings, no objetos
            JsonNode imageUrls = rootNode.get("imageUrls");
            if (imageUrls.isArray() && imageUrls.size() > 0) {
                JsonNode firstImageUrl = imageUrls.get(0);
                assert firstImageUrl.isTextual() : "imageUrls debe contener solo strings, no objetos";
            }

            System.out.println("✅ Estructura JSON de getPostById es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testGetPostsByUserIdJsonStructure() throws Exception {
        // Usar un userId que probablemente exista (1)
        MvcResult result = mockMvc.perform(get("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Verificar que es un array
        assert rootNode.isArray() : "La respuesta debe ser un array";

        if (rootNode.size() > 0) {
            JsonNode firstPost = rootNode.get(0);

            // Verificar estructura esperada de PostResponseDTO
            assert firstPost.has("id") : "Post debe tener id";
            assert firstPost.has("title") : "Post debe tener title";
            assert firstPost.has("message") : "Post debe tener message";
            assert firstPost.has("userUsername") : "Post debe tener userUsername";
            assert firstPost.has("tags") : "Post debe tener tags";

            // Verificar que NO tenga campos innecesarios
            assert !firstPost.has("user") : "Post NO debe incluir user completo";
            assert !firstPost.has("comments") : "Post NO debe incluir comments completos";
            assert !firstPost.has("postLoves") : "Post NO debe incluir postLoves completos";

            System.out.println("✅ Estructura JSON de getPostsByUserId es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testJsonResponseSizeIsReasonable() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        int jsonSize = jsonResponse.length();

        // El JSON no debería ser excesivamente grande (menos de 100KB para una página de posts)
        assert jsonSize < 100000 : "El JSON de posts es demasiado grande: " + jsonSize + " caracteres";

        System.out.println("✅ Tamaño del JSON es razonable: " + jsonSize + " caracteres");
    }

    @Test
    public void testNoCircularReferencesInJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        // Verificar que no hay referencias circulares contando la profundidad
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        int maxDepth = calculateMaxDepth(rootNode);

        // La profundidad no debería ser excesiva (máximo 5 niveles)
        assert maxDepth <= 5 : "JSON tiene demasiada profundidad: " + maxDepth + " niveles";

        System.out.println("✅ No hay referencias circulares. Profundidad máxima: " + maxDepth);
    }

    private int calculateMaxDepth(JsonNode node) {
        if (node.isValueNode()) {
            return 1;
        }

        int maxChildDepth = 0;
        if (node.isArray()) {
            for (JsonNode child : node) {
                maxChildDepth = Math.max(maxChildDepth, calculateMaxDepth(child));
            }
        } else if (node.isObject()) {
            for (JsonNode child : node) {
                maxChildDepth = Math.max(maxChildDepth, calculateMaxDepth(child));
            }
        }

        return maxChildDepth + 1;
    }
}
