package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class ImageToHexSqlGenerator {

    public static void main(String[] args) {
        String imagesDir = "D:/GITHUB/ÁGORA/Proyecto Personal/agora_frontend/public/images/img";
        String outputSql = "D:/GITHUB/ÁGORA/Proyecto Personal/agora_back/src/main/resources/data_images.sql";
        File dir = new File(imagesDir);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("-- SQL para insertar imágenes convertidas a HEX\n");
        sqlBuilder.append("INSERT INTO event_images (image_name, image_data, event_id) VALUES\n");
        int eventId = 1;
        boolean first = true;
        for (File file : dir.listFiles()) {
            if (file.isFile() && isImage(file.getName())) {
                String hex = fileToHex(file.getAbsolutePath());
                if (!first) {
                    sqlBuilder.append(",\n");
                }
                first = false;
                sqlBuilder.append(String.format(Locale.US,
                        "  ('%s', UNHEX('%s'), %d)",
                        file.getName(), hex, eventId
                ));
                eventId++;
            }
        }
        sqlBuilder.append(";\n");
        try {
            Files.write(Paths.get(outputSql), sqlBuilder.toString().getBytes());
            System.out.println("Archivo SQL generado: " + outputSql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isImage(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif");
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
            e.printStackTrace();
            return "";
        }
    }
}
