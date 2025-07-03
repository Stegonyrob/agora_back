package de.stella.agora_web.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.EventImageDTO;

public interface IEventImageService {

    EventImageDTO getEventImageById(Long id);

    List<EventImageDTO> getImagesByEventId(Long eventId);

    EventImageDTO saveEventImage(EventImageDTO dto);

    void deleteEventImage(Long id);

    void deleteImagesByEventId(Long id);

    // ✅ NUEVO MÉTODO PARA MANEJAR MULTIPART FILES
    List<EventImageDTO> processAndSaveImages(MultipartFile[] files, Long eventId);

    // ✅ NUEVO MÉTODO PARA VALIDAR ARCHIVOS
    boolean isValidImageFile(MultipartFile file);
}
