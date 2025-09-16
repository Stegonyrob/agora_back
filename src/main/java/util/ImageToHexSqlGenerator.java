package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImageToHexSqlGenerator {

    // Mapeo de imágenes disponibles a text_id y descripción
    private static final Map<String, ImageInfo> IMAGE_MAPPING = new HashMap<>();

    static class ImageInfo {

        int textId;
        String description;

        ImageInfo(int textId, String description) {
            this.textId = textId;
            this.description = description;
        }
    }

    static {
        // Mapeo basado en las imágenes que tienes (ajusta según tus archivos reales)
        IMAGE_MAPPING.put("alumnosOrdenador.jpg", new ImageInfo(1, "Estudiantes trabajando con ordenadores"));
        IMAGE_MAPPING.put("fachada.jpg", new ImageInfo(2, "Fachada del centro educativo"));
        IMAGE_MAPPING.put("adolescentesGrupal.jpg", new ImageInfo(3, "Adolescentes en actividad grupal"));
        IMAGE_MAPPING.put("ninaFicha.jpg", new ImageInfo(4, "Niña completando una ficha de trabajo"));
        IMAGE_MAPPING.put("ninoCascos.jpg", new ImageInfo(8, "Niño con cascos de audio"));
        IMAGE_MAPPING.put("profesional.jpg", new ImageInfo(11, "Profesional de la educación"));

    }

    public static void main(String[] args) {
        String imagesDir = "D:/GITHUB/ÁGORA/Proyecto Personal/agora_frontend/public/images/img";
        String outputSql = "D:/GITHUB/ÁGORA/Proyecto Personal/agora_back/src/main/resources/insert_text_images.sql";

        File dir = new File(imagesDir);

        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("❌ Directorio no encontrado: " + imagesDir);
            return;
        }

        File[] imageFiles = dir.listFiles(file -> file.isFile() && isImage(file.getName()));

        if (imageFiles == null || imageFiles.length == 0) {
            System.err.println("❌ No se encontraron imágenes en: " + imagesDir);
            return;
        }

        System.out.println("=== PROCESANDO IMÁGENES PARA TEXT_IMAGES ===");
        System.out.println("📁 Directorio: " + imagesDir);
        System.out.println("📊 Imágenes encontradas: " + imageFiles.length);

        // Mostrar archivos encontrados
        System.out.println("\n📋 Archivos encontrados:");
        Arrays.stream(imageFiles)
                .forEach(file -> System.out.println("  - " + file.getName()));

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("-- Insertar imágenes reales para text_images\n");
        sqlBuilder.append("INSERT INTO text_images (text_id, image_name, image_data, created_at) VALUES\n");

        boolean first = true;
        int processedCount = 0;
        Map<String, Integer> assignedImages = new HashMap<>();

        // Procesar imágenes disponibles
        for (File file : imageFiles) {
            String fileName = file.getName();
            ImageInfo imageInfo = IMAGE_MAPPING.get(fileName);

            if (imageInfo == null) {
                // Asignar automáticamente a text_ids disponibles
                imageInfo = findAvailableTextId(assignedImages);
                if (imageInfo == null) {
                    System.out.printf("[SKIP] %s - No hay text_id disponible\n", fileName);
                    continue;
                }
                System.out.printf("[AUTO] %s → text_id %d (asignación automática)\n",
                        fileName, imageInfo.textId);
            } else {
                System.out.printf("[MAPPED] %s → text_id %d\n", fileName, imageInfo.textId);
            }

            String hex = fileToHex(file.getAbsolutePath());
            if (hex.isEmpty()) {
                System.out.printf("[ERROR] %s - No se pudo convertir a HEX\n", fileName);
                continue;
            }

            String imageUrl = "/api/v1/images/temp_images/" + fileName;

            if (!first) {
                sqlBuilder.append(",\n");
            }

            sqlBuilder.append(String.format(Locale.US,
                    "  (%d, '%s', '%s', UNHEX('%s'), '%s', NOW())",
                    imageInfo.textId, fileName, imageUrl, hex, imageInfo.description
            ));

            assignedImages.put(fileName, imageInfo.textId);
            first = false;
            processedCount++;
        }

        sqlBuilder.append(";\n\n");
        sqlBuilder.append("-- Total de imágenes procesadas: ").append(processedCount).append("\n");
        sqlBuilder.append("-- Imágenes asignadas:\n");

        assignedImages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    sqlBuilder.append("-- text_id ").append(entry.getValue())
                            .append(" → ").append(entry.getKey()).append("\n");
                });

        try {
            Files.write(Paths.get(outputSql), sqlBuilder.toString().getBytes());

            System.out.println("\n=== RESUMEN ===");
            System.out.println("✅ Archivo SQL generado: " + outputSql);
            System.out.println("📊 Total imágenes procesadas: " + processedCount);
            System.out.println("📋 Archivo generado listo para usar");

            if (processedCount < 23) {
                System.out.println("\n⚠️  NOTA: Solo se procesaron " + processedCount + " de 23 text_ids necesarios");
                System.out.println("💡 Considera añadir más imágenes o usar placeholders para los text_ids faltantes");
            }

        } catch (IOException e) {
            System.err.println("❌ Error escribiendo archivo SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ImageInfo findAvailableTextId(Map<String, Integer> assignedImages) {
        // Buscar text_ids del 1-23 que no estén asignados
        for (int textId = 1; textId <= 23; textId++) {
            boolean isAssigned = assignedImages.values().contains(textId);
            if (!isAssigned) {
                return new ImageInfo(textId, "Imagen educativa " + textId);
            }
        }
        return null; // No hay text_ids disponibles
    }

    private static boolean isImage(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg")
                || lower.endsWith(".jpeg")
                || lower.endsWith(".png")
                || lower.endsWith(".gif")
                || lower.endsWith(".webp");
    }

    private static String fileToHex(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] bytes = fis.readAllBytes();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + filePath + " - " + e.getMessage());
            return "";
        }
    }
}
