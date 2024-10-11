package de.stella.agora_web.avatar.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.avatar.exceptions.StorageException;
import de.stella.agora_web.avatar.exceptions.StorageFileNotFoundException;
import de.stella.agora_web.avatar.repository.ImageRepository;
import de.stella.agora_web.avatar.service.storage.IStorageService;
import de.stella.agora_web.config.StorageProperties;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.utilities.Time;

@Service
public class ImageServiceImpl implements IStorageService {

    private final Path rootLocation;
    @SuppressWarnings("unused")
    private final ImageRepository imageRepository;
    @SuppressWarnings("unused")
    private final ProfileRepository profileRepository;
    @SuppressWarnings("unused")
    private final Time time;

    public ImageServiceImpl(ImageRepository imageRepository, ProfileRepository profileRepository, Time time,
            StorageProperties properties) throws IOException {
        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException("File upload location cannot be empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
        this.imageRepository = imageRepository;
        this.profileRepository = profileRepository;
        this.time = time;

        // Initialize storage if necessary
        Files.createDirectories(this.rootLocation);
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = this.rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return (Resource) resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void init() {
        // Initialization logic here
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path fileToDelete = this.rootLocation.resolve(filename);
            FileSystemUtils.deleteRecursively(fileToDelete);
        } catch (IOException e) {
            throw new StorageException("Could not delete file: " + filename, e);
        }
        return false;
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(this.rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not delete all files in the directory", e);
        }
    }

    @Override
    public void saveImages(Long userId, MultipartFile[] images) {
        Arrays.asList(images).forEach(image -> {
            String imageName = StringUtils.cleanPath(image.getOriginalFilename());
            try {
                Files.copy(image.getInputStream(), this.rootLocation.resolve(imageName));
            } catch (IOException e) {
                throw new StorageException("Could not save image file: " + imageName, e);
            }
        });
    }

    @Override
    public Resource loadAsResource(String filename) {
        return load(filename);
    }

    @Override
    public void saveMainImage(Long userId, MultipartFile mainImage) {
        String imageName = StringUtils.cleanPath(mainImage.getOriginalFilename());
        try {
            Files.copy(mainImage.getInputStream(), this.rootLocation.resolve(imageName));
        } catch (IOException e) {
            throw new StorageException("Could not save main image file: " + imageName, e);
        }
    }

}