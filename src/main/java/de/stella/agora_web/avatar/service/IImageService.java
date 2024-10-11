package de.stella.agora_web.avatar.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.avatar.exceptions.ImageNotFoundException;
import de.stella.agora_web.profiles.exceptions.ProfileNotFoundException;

@Service
public interface IImageService {

    void init();

    void saveMainImage(Long profileId, MultipartFile file) throws IOException, ProfileNotFoundException;

    void deleteMainImage(Long profileId) throws IOException, ProfileNotFoundException, ImageNotFoundException;

    void editMainImage(Long profileId, MultipartFile file)
            throws IOException, ProfileNotFoundException, ImageNotFoundException;

    Path loadMainImage(Long profileId) throws IOException, ProfileNotFoundException, ImageNotFoundException;

    boolean delete(String filename);

    void deleteAll();

    Stream<Path> loadAll();

    List<Path> loadAllByProfileId(Long profileId) throws IOException, ProfileNotFoundException;
}
