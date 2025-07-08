package de.stella.agora_web.events.controller;

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
 * Tests para verificar que los endpoints de events devuelvan JSON optimizados
 * sin exceso de datos ni redundancia.
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("h2")
public class EventControllerJsonTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllEventsJsonStructure() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events")
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
            JsonNode firstEvent = content.get(0);

            // Verificar estructura esperada de EventSummaryDTO o similar
            assert firstEvent.has("id") : "Event debe tener id";
            assert firstEvent.has("title") : "Event debe tener title";
            assert firstEvent.has("message") : "Event debe tener message";
            assert firstEvent.has("creationDate") : "Event debe tener creationDate";
            assert firstEvent.has("capacity") : "Event debe tener capacity";

            // Verificar que NO tenga campos innecesarios
            assert !firstEvent.has("user") : "Event NO debe incluir user completo";
            assert !firstEvent.has("participants") : "Event NO debe incluir participants completos";
            assert !firstEvent.has("images") : "Event NO debe incluir images completas con datos binarios";
            assert !firstEvent.has("eventImages") : "Event NO debe incluir eventImages completas";

            System.out.println("✅ Estructura JSON de getAllEvents es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testGetFavoriteEventsJsonStructure() throws Exception {
        // Usar un userId que probablemente exista (1)
        MvcResult result = mockMvc.perform(get("/api/users/1/favorite-events")
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

            // Verificar estructura esperada
            assert firstEvent.has("id") : "Event debe tener id";
            assert firstEvent.has("title") : "Event debe tener title";
            assert firstEvent.has("message") : "Event debe tener message";

            // Verificar que NO tenga campos innecesarios
            assert !firstEvent.has("user") : "Event NO debe incluir user completo";
            assert !firstEvent.has("participants") : "Event NO debe incluir participants completos";
            assert !firstEvent.has("images") : "Event NO debe incluir images completas";

            System.out.println("✅ Estructura JSON de getFavoriteEvents es correcta y optimizada");
            System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
        }
    }

    @Test
    public void testGetEventByIdJsonStructure() throws Exception {
        // Usar un ID de evento que probablemente exista (1)
        MvcResult result = mockMvc.perform(get("/api/events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode eventNode = objectMapper.readTree(jsonResponse);

        // Verificar estructura esperada de EventDetailDTO o similar
        assert eventNode.has("id") : "Event debe tener id";
        assert eventNode.has("title") : "Event debe tener title";
        assert eventNode.has("message") : "Event debe tener message";
        assert eventNode.has("creationDate") : "Event debe tener creationDate";
        assert eventNode.has("capacity") : "Event debe tener capacity";

        // Verificar que NO tenga campos innecesarios
        assert !eventNode.has("user") : "Event NO debe incluir user completo";
        assert !eventNode.has("participants") : "Event NO debe incluir participants completos";
        assert !eventNode.has("images") : "Event NO debe incluir images completas con datos binarios";
        assert !eventNode.has("eventImages") : "Event NO debe incluir eventImages completas";

        System.out.println("✅ Estructura JSON de getEventById es correcta y optimizada");
        System.out.println("📊 Tamaño del JSON: " + jsonResponse.length() + " caracteres");
    }

    @Test
    public void testJsonResponseSizeIsReasonable() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        int jsonSize = jsonResponse.length();

        // El JSON no debería ser excesivamente grande (menos de 100KB para una página de events)
        assert jsonSize < 100000 : "El JSON de events es demasiado grande: " + jsonSize + " caracteres";

        // Verificar que el JSON no contenga referencias nulas
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        assert !jsonNode.has("user") : "Event NO debe incluir user completo";
        assert !jsonNode.has("participants") : "Event NO debe incluir participants completos";
        assert !jsonNode.has("images") : "Event NO debe incluir images completas con datos binarios";
        assert !jsonNode.has("eventImages") : "Event NO debe incluir eventImages completas";

        System.out.println("✅ Tamaño del JSON es razonable: " + jsonSize + " caracteres");
    }

    @Test
    public void testNoCircularReferencesInJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events")
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
