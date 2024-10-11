package de.stella.agora_web.avatar.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface IStorageService {

    void init();

    void saveMainImage(@NonNull Long id, MultipartFile file);

    void saveImages(@NonNull Long id, MultipartFile[] file);

    Resource load(String filename);

    Resource loadAsResource(String filename);

    public boolean delete(String filename);

    void deleteAll();

}