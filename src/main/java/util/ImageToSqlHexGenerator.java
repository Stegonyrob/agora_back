package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageToSqlHexGenerator {

    private static final String IMAGE_DIR = "D:/GITHUB/ÁGORA/Proyecto Personal/agora_frontend/public/images/img";
    private static final String OPTIMIZED_DIR = IMAGE_DIR + "/img_optimizada";
    private static final String OUTPUT_SQL = "D:/GITHUB/ÁGORA/Proyecto Personal/agora_back/src/main/resources/insert_images.sql";
    private static final int TEXT_ID = 1; // Cambia según tu lógica
    private static final long MIN_SIZE = 1000; // bytes
    private static final long MAX_SIZE = 500_000; // 500 KB

    public static void main(String[] args) {
        try {
            File dir = new File(IMAGE_DIR);
            File outDir = new File(OPTIMIZED_DIR);
            if (!dir.exists() || !dir.isDirectory()) {
                System.err.println("Directorio de imágenes no encontrado: " + IMAGE_DIR);
                return;
            }
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO text_images (text_id, image_data) VALUES\n");
            boolean first = true;
            int count = 0;
            for (File file : dir.listFiles()) {
                if (!file.isFile()) {
                    continue;
                }
                String name = file.getName().toLowerCase(Locale.ROOT);
                if (!(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))) {
                    continue;
                }
                File optimized = new File(outDir, file.getName());
                long size = file.length();
                byte[] data;
                boolean needsValidation = false;

                if (size > MAX_SIZE) {
                    // Intentar comprimir la imagen
                    data = compressImage(file, optimized, MAX_SIZE);
                    if (data == null) {
                        System.out.printf("[ERROR] %s no pudo ser comprimida por debajo de %d bytes.\n", file.getName(), MAX_SIZE);
                        continue;
                    }
                    System.out.printf("[INFO] %s fue comprimida a %d bytes.\n", file.getName(), data.length);
                    needsValidation = true; // Solo validar imágenes comprimidas
                } else {
                    // Copiar imagen tal cual si está en rango (≤ 5MB)
                    Files.copy(file.toPath(), optimized.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    data = Files.readAllBytes(optimized.toPath());
                    System.out.printf("[INFO] %s usada sin comprimir (%d bytes).\n", file.getName(), data.length);
                    // No necesita validación adicional porque ya cumple ser ≤ 5MB
                }

                // Solo validar rango para imágenes que fueron comprimidas
                if (needsValidation && (data.length < MIN_SIZE || data.length > MAX_SIZE)) {
                    System.out.printf("[ADVERTENCIA] %s tamaño %d bytes fuera de rango tras comprimir. No se incluirá en el SQL.\n", file.getName(), data.length);
                    continue;
                }
                String hex = bytesToHex(data);
                if (!first) {
                    sql.append(",\n");
                }
                sql.append(String.format("  ('%s', UNHEX('%s'), %d)", file.getName(), hex, TEXT_ID));
                first = false;
                count++;
            }
            if (count > 0) {
                sql.append(";\n");
                try (FileOutputStream fos = new FileOutputStream(OUTPUT_SQL)) {
                    fos.write(sql.toString().getBytes());
                }
                System.out.println("Archivo SQL generado en: " + OUTPUT_SQL);
            } else {
                System.out.println("No se encontraron imágenes válidas para insertar.");
            }
        } catch (IOException e) {
            System.err.println("Error en la ejecución principal: " + e.getMessage());
        }
    }

    /**
     * Comprime una imagen JPEG/PNG a un tamaño máximo en bytes.
     *
     * @return array de bytes de la imagen comprimida o null si no se pudo.
     */
    private static byte[] compressImage(File input, File output, long maxBytes) {
        try {
            BufferedImage image = ImageIO.read(input);
            float quality = 0.85f;
            byte[] data;
            for (int i = 0; i < 10; i++) { // 10 intentos de compresión
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                if (!writers.hasNext()) {
                    return null;
                }
                ImageWriter writer = writers.next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(quality);
                }
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(image, null, null), param);
                }
                writer.dispose();
                data = baos.toByteArray();
                if (data.length <= maxBytes) {
                    Files.write(output.toPath(), data);
                    return data;
                }
                quality -= 0.10f;
                if (quality < 0.1f) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Fallo al comprimir: " + input.getName() + " " + e.getMessage());
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
