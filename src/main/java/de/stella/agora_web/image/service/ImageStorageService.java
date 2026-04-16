package de.stella.agora_web.image.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "d:/GITHUB/ÁGORA/Proyecto Personal/agora_back/temp_images/";

    public String storeImage(MultipartFile file) {
        try {
            // Generate a unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

            // Define the target path
            Path targetPath = Paths.get(UPLOAD_DIR + uniqueFilename);

            // Copy the file to the target directory
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path to be stored in the database
            return "/images/" + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image: " + file.getOriginalFilename(), e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg"; // Default to jpg if no extension is found
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    public byte[] loadImage(String path) {
        try {
            Path imagePath = Paths.get(UPLOAD_DIR + path);
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image from path: " + path, e);
        }
    }
}
