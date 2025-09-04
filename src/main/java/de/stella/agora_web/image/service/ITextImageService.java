package de.stella.agora_web.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.TextImageDTO;

public interface ITextImageService {

    List<TextImageDTO> getImagesByTextId(Long textId);

    TextImageDTO getTextImageById(Long id);

    byte[] getTextImageData(Long id);

    TextImageDTO createTextImage(TextImageDTO dto);

    List<TextImageDTO> processAndSaveImages(MultipartFile[] files, Long textId);

    void deleteTextImage(Long id);

    void deleteMultipleTextImages(List<Long> imageIds);
}
