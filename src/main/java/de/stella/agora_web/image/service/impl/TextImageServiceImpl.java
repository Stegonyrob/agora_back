package de.stella.agora_web.image.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import de.stella.agora_web.image.service.ITextImageService;

@Service
public class TextImageServiceImpl implements ITextImageService {

    @Override
    public List<TextImageDTO> getImagesByTextId(Long textId) {
        // TODO: Implementar lógica de obtención
        return List.of();
    }

    @Override
    public TextImageDTO getTextImageById(Long id) {
        // TODO: Implementar lógica de obtención
        return null;
    }

    @Override
    public byte[] getTextImageData(Long id) {
        // TODO: Implementar lógica de obtención de datos binarios
        return new byte[0];
    }

    @Override
    public TextImageDTO createTextImage(TextImageDTO dto) {
        // TODO: Implementar lógica de creación
        return dto;
    }

    @Override
    public List<TextImageDTO> processAndSaveImages(MultipartFile[] files, Long textId) {
        // TODO: Implementar lógica de guardado múltiple
        return List.of();
    }

    @Override
    public void deleteTextImage(Long id) {
        // TODO: Implementar lógica de borrado
    }

    @Override
    public void deleteMultipleTextImages(List<Long> imageIds) {
        // TODO: Implementar lógica de borrado múltiple
    }
}
