package de.stella.agora_web.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.TextImageDTO;

/**
 * Interfaz para el servicio de imágenes de texto siguiendo principios SOLID
 * SRP: Responsabilidad única de definir operaciones de imágenes de texto ISP:
 * Interfaz segregada - solo métodos necesarios para imágenes de texto DIP:
 * Abstracción que permite diferentes implementaciones
 */
public interface ITextImageService {

    List<TextImageDTO> getAllTextImages();

    List<TextImageDTO> getImagesByTextId(Long textId);

    TextImageDTO getTextImageById(Long id);

    TextImageDTO createTextImage(TextImageDTO dto);

    List<TextImageDTO> processAndSaveImages(MultipartFile[] files, Long textId);

    TextImageDTO updateTextImage(Long id, TextImageDTO dto);

    void deleteTextImage(Long id);

    void deleteMultipleTextImages(List<Long> imageIds);

    void deleteImagesByTextId(Long textId);

    /**
     * Obtiene la ruta de la imagen de texto por su ID.
     *
     * @param id Identificador de la imagen de texto.
     * @return Ruta de la imagen de texto.
     */
    String getTextImagePath(Long id);
}
