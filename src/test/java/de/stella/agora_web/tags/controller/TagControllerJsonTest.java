package de.stella.agora_web.tags.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests para verificar que los endpoints de tags devuelvan JSON optimizados sin
 * exceso de datos ni redundancia.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class TagControllerJsonTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTagsJsonStructure() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/all/tags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Verificar que es un array
        assert rootNode.isArray() : "La respuesta debe ser un array";

        if (rootNode.size() > 0) {
            JsonNode firstTag = rootNode.get(0);

            // Verificar estructura esperada de TagSummaryDTO
            assert firstTag.has("id") : "Tag debe tener id";
            assert firstTag.has("name") : "Tag debe tener name";
            assert firstTag.has("archived") : "Tag debe tener archived";

            // Verificar que NO tenga campos innecesarios
            assert !firstTag.has("posts") : "Tag NO debe incluir posts completos";
            assert !firstTag.has("events") : "Tag NO debe incluir events completos";
            assert !firstTag.has("creationDate") : "Tag NO debe incluir campos innecesarios";

            // Verificar tipos
            assert firstTag.get("id").isNumber() : "id debe ser numérico";
            assert firstTag.get("name").isTextual() : "name debe ser texto";
            assert firstTag.get("archived").isBoolean() : "archived debe ser boolean";

            System.out.println("✅ Estructura JSON de getAllTags es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testGetEventsByTagNameJsonStructure() throws Exception {
        // Usar un tag que probablemente exista
        MvcResult result = mockMvc.perform(get("/api/all/tags/events/neurodiversidad")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Verificar que es un array
        assert rootNode.isArray() : "La respuesta debe ser un array";

        if (rootNode.size() > 0) {
            JsonNode firstEvent = rootNode.get(0);

            // Verificar estructura esperada de EventSummaryDTO
            assert firstEvent.has("id") : "Event debe tener id";
            assert firstEvent.has("title") : "Event debe tener title";
            assert firstEvent.has("message") : "Event debe tener message";
            assert firstEvent.has("creationDate") : "Event debe tener creationDate";
            assert firstEvent.has("capacity") : "Event debe tener capacity";
            assert firstEvent.has("tags") : "Event debe tener tags";

            // Verificar que NO tenga campos innecesarios
            assert !firstEvent.has("user") : "Event NO debe incluir user completo";
            assert !firstEvent.has("participants") : "Event NO debe incluir participants completos";
            assert !firstEvent.has("images") : "Event NO debe incluir images completas";

            // Verificar estructura de tags
            JsonNode tags = firstEvent.get("tags");
            if (tags.isArray() && tags.size() > 0) {
                JsonNode firstTag = tags.get(0);
                assert firstTag.has("id") : "Tag dentro de event debe tener id";
                assert firstTag.has("name") : "Tag dentro de event debe tener name";
                assert !firstTag.has("posts") : "Tag dentro de event NO debe incluir posts";
                assert !firstTag.has("events") : "Tag dentro de event NO debe incluir events";
            }

            System.out.println("✅ Estructura JSON de getEventsByTagName es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testGetPostsByTagNameJsonStructure() throws Exception {
        // Usar un tag que probablemente exista
        MvcResult result = mockMvc.perform(get("/api/any/tags/posts/neurodiversidad")
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

            // Verificar estructura esperada de PostSummaryDTO
            assert firstPost.has("id") : "Post debe tener id";
            assert firstPost.has("title") : "Post debe tener title";
            assert firstPost.has("message") : "Post debe tener message";
            assert firstPost.has("creationDate") : "Post debe tener creationDate";
            assert firstPost.has("archived") : "Post debe tener archived";
            assert firstPost.has("userUsername") : "Post debe tener userUsername";
            assert firstPost.has("tags") : "Post debe tener tags";

            // Verificar que NO tenga campos innecesarios
            assert !firstPost.has("user") : "Post NO debe incluir user completo";
            assert !firstPost.has("comments") : "Post NO debe incluir comments completos";
            assert !firstPost.has("replies") : "Post NO debe incluir replies completos";
            assert !firstPost.has("postLoves") : "Post NO debe incluir postLoves completos";

            // Verificar estructura de tags
            JsonNode tags = firstPost.get("tags");
            if (tags.isArray() && tags.size() > 0) {
                JsonNode firstTag = tags.get(0);
                assert firstTag.has("id") : "Tag dentro de post debe tener id";
                assert firstTag.has("name") : "Tag dentro de post debe tener name";
                assert !firstTag.has("posts") : "Tag dentro de post NO debe incluir posts";
                assert !firstTag.has("events") : "Tag dentro de post NO debe incluir events";
            }

            System.out.println("✅ Estructura JSON de getPostsByTagName es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testJsonResponseSizeIsReasonable() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/all/tags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        int jsonSize = jsonResponse.length();

        // El JSON no debería ser excesivamente grande (menos de 50KB para una lista de tags)
        assert jsonSize < 50000 : "El JSON de tags es demasiado grande: " + jsonSize + " caracteres";

        System.out.println("✅ Tamaño del JSON es razonable: " + jsonSize + " caracteres");
    }
}
