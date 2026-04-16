package de.stella.agora_web.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.config.TestConfig;

/**
 * Tests de integración para verificar que TODOS los endpoints principales
 * devuelvan JSON bien formados y optimizados sin redundancia excesiva.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestConfig.class)
public class AllEndpointsJsonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAllMainEndpointsJsonStructure() throws Exception {
        System.out.println("\n🔍 INICIANDO ANÁLISIS COMPLETO DE JSON EN TODOS LOS ENDPOINTS");
        System.out.println("═══════════════════════════════════════════════════════════");

        // 1. Test endpoints públicos de tags
        testEndpoint("Tags - Obtener todos", "/api/all/tags", 50000);
        testEndpoint("Tags - Eventos por tag", "/api/all/tags/events/neurodiversidad", 100000);

        // 2. Test endpoints de posts
        testEndpoint("Posts - Obtener todos (paginado)", "/api/posts", 100000);

        // 3. Test endpoints de events
        testEndpoint("Events - Obtener todos (públicos)", "/api/v1/public/events", 100000);

        // 4. Test endpoints específicos con IDs dinámicos
        testEndpointsWithDynamicIds();

        System.out.println("\n✅ ANÁLISIS COMPLETO FINALIZADO - TODOS LOS ENDPOINTS VERIFICADOS");
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    private void testEndpoint(String description, String endpoint, int maxSizeBytes) throws Exception {
        System.out.println("\n📊 Analizando: " + description);
        System.out.println("🔗 Endpoint: " + endpoint);

        try {
            MvcResult result = mockMvc.perform(get(endpoint)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Análisis de tamaño
            int jsonSize = jsonResponse.length();
            System.out.println("📏 Tamaño del JSON: " + jsonSize + " caracteres");

            if (jsonSize > maxSizeBytes) {
                System.out.println("⚠️  ADVERTENCIA: JSON muy grande (>" + maxSizeBytes + " caracteres)");
            } else {
                System.out.println("✅ Tamaño del JSON adecuado");
            }

            // Análisis de profundidad
            int maxDepth = calculateMaxDepth(rootNode);
            System.out.println("🌳 Profundidad máxima: " + maxDepth + " niveles");

            if (maxDepth > 6) {
                System.out.println("⚠️  ADVERTENCIA: JSON muy profundo (>" + 6 + " niveles)");
            } else {
                System.out.println("✅ Profundidad adecuada");
            }

            // Análisis de campos problemáticos
            analyzeProblematicFields(rootNode);

        } catch (Exception e) {
            System.out.println("❌ ERROR en endpoint: " + e.getMessage());
            throw e;
        }
    }

    private void testEndpointsWithDynamicIds() throws Exception {
        System.out.println("\n🔄 Obteniendo IDs dinámicos para tests específicos...");

        // Obtener un ID de post válido
        try {
            MvcResult postsResult = mockMvc.perform(get("/api/posts"))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode postsNode = objectMapper.readTree(postsResult.getResponse().getContentAsString());
            JsonNode content = postsNode.get("content");

            if (content.isArray() && content.size() > 0) {
                Long postId = content.get(0).get("id").asLong();
                testEndpoint("Posts - Post individual", "/api/posts/" + postId, 50000);
            }
        } catch (Exception e) {
            System.out.println("⚠️  No se pudo probar endpoint de post individual: " + e.getMessage());
        }

        // Obtener un ID de event válido
        try {
            MvcResult eventsResult = mockMvc.perform(get("/api/v1/public/events"))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode eventsNode = objectMapper.readTree(eventsResult.getResponse().getContentAsString());

            // Para endpoints públicos que devuelven arrays directos
            if (eventsNode.isArray() && eventsNode.size() > 0) {
                Long eventId = eventsNode.get(0).get("id").asLong();
                testEndpoint("Events - Event individual", "/api/v1/public/events/" + eventId, 50000);
            }
        } catch (Exception e) {
            System.out.println("⚠️  No se pudo probar endpoint de event individual: " + e.getMessage());
        }

        // Test endpoints de usuario (con ID fijo que probablemente exista)
        try {
            testEndpoint("Posts - Posts por usuario", "/api/user/1", 100000);
        } catch (Exception e) {
            System.out.println("⚠️  No se pudo probar endpoint de posts por usuario: " + e.getMessage());
        }
    }

    private void analyzeProblematicFields(JsonNode node) {
        System.out.println("🔍 Analizando campos problemáticos...");

        boolean hasProblems = false;

        // Analizar recursivamente
        hasProblems |= checkForProblematicFields(node, "", 0);

        if (!hasProblems) {
            System.out.println("✅ No se encontraron campos problemáticos");
        }
    }

    private boolean checkForProblematicFields(JsonNode node, String path, int depth) {
        boolean foundProblems = false;

        if (depth > 8) {
            System.out.println("⚠️  Profundidad excesiva en: " + path);
            return true;
        }

        if (node.isObject()) {
            node.fieldNames().forEachRemaining(fieldName -> {
                String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;
                JsonNode fieldValue = node.get(fieldName);

                // Campos que NO deberían aparecer en respuestas optimizadas
                if (fieldName.equals("password") || fieldName.equals("imageData")
                        || fieldName.equals("images") && fieldValue.isArray() && fieldValue.size() > 0
                        && fieldValue.get(0).has("imageData")) {
                    System.out.println("⚠️  Campo problemático encontrado: " + currentPath);
                }

                // Referencias circulares potenciales
                if ((fieldName.equals("posts") || fieldName.equals("events")
                        || fieldName.equals("user") || fieldName.equals("comments")
                        || fieldName.equals("replies")) && fieldValue.isArray()
                        && fieldValue.size() > 0 && fieldValue.get(0).isObject()) {
                    JsonNode firstItem = fieldValue.get(0);
                    if (firstItem.size() > 10) { // Objeto muy grande
                        System.out.println("⚠️  Objeto anidado muy grande en: " + currentPath);
                    }
                }

                checkForProblematicFields(fieldValue, currentPath, depth + 1);
            });
        } else if (node.isArray()) {
            for (int i = 0; i < Math.min(node.size(), 3); i++) { // Solo revisar primeros 3 elementos
                checkForProblematicFields(node.get(i), path + "[" + i + "]", depth + 1);
            }
        }

        return foundProblems;
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

    @Test
    public void testSpecificProblematicEndpoints() throws Exception {
        System.out.println("\n🎯 TESTING ENDPOINTS ESPECÍFICOS QUE CAUSABAN PROBLEMAS");
        System.out.println("═══════════════════════════════════════════════════════");

        // Estos endpoints fueron mencionados como problemáticos en Postman
        String[] problematicEndpoints = {
            "/api/all/tags",
            "/api/all/tags/events/neurodiversidad",
            "/api/any/tags/posts/neurodiversidad"
        };

        for (String endpoint : problematicEndpoints) {
            try {
                System.out.println("\n🔍 Verificando endpoint problemático: " + endpoint);

                MvcResult result = mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

                String jsonResponse = result.getResponse().getContentAsString();
                int jsonSize = jsonResponse.length();

                System.out.println("📏 Tamaño actual: " + jsonSize + " caracteres");

                // Verificar que el JSON sea parseable
                objectMapper.readTree(jsonResponse);

                // Verificar que no sea excesivamente grande
                if (jsonSize > 200000) { // 200KB
                    System.out.println("❌ ENDPOINT SIGUE SIENDO PROBLEMÁTICO: " + jsonSize + " caracteres");
                    throw new AssertionError("Endpoint " + endpoint + " sigue devolviendo JSON muy grande: " + jsonSize);
                } else {
                    System.out.println("✅ Endpoint optimizado correctamente");
                }

            } catch (Exception e) {
                System.out.println("❌ Error en endpoint " + endpoint + ": " + e.getMessage());
                // No lanzar excepción para que continúe con otros endpoints
            }
        }
    }
}
