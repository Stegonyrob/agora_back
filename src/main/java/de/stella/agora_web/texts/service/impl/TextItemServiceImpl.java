package de.stella.agora_web.texts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.texts.controller.dto.TextItemDTO;
import de.stella.agora_web.texts.model.TextItem;
import de.stella.agora_web.texts.repository.TextItemRepository;
import de.stella.agora_web.texts.service.ITextItemService;

@Service
public class TextItemServiceImpl implements ITextItemService {

    @Autowired
    private TextItemRepository textItemRepository;

    @Override
    public List<TextItemDTO> getAllTexts() {
        return textItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TextItemDTO getTextById(Long id) {
        return textItemRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Text not found"));
    }

    @Override
    public TextItemDTO createText(TextItemDTO textItemDTO) {
        TextItem textItem = convertToEntity(textItemDTO);
        TextItem savedText = textItemRepository.save(textItem);
        return convertToDTO(savedText);
    }

    @Override
    public TextItemDTO updateText(Long id, TextItemDTO dto) {
        TextItem item = textItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text not found"));
        item.setTitle(dto.getTitle());
        item.setMessage(dto.getMessage());
        item.setCategory(dto.getCategory());
        // No se maneja image como string, las imágenes se gestionan por separado
        TextItem updatedItem = textItemRepository.save(item);
        return convertToDTO(updatedItem);
    }

    @Override
    public void deleteText(Long id) {
        textItemRepository.deleteById(id);
    }

    /**
     * Convierte un TextItem a DTO SIN imágenes (patrón consistente con Posts y Events).
     * Las imágenes se obtienen por separado para eficiencia.
     */
    private TextItemDTO convertToDTO(TextItem textItem) {
        TextItemDTO dto = new TextItemDTO();
        dto.setId(textItem.getId());
        dto.setCategory(textItem.getCategory());
        dto.setTitle(textItem.getTitle());
        dto.setMessage(textItem.getMessage());
        // NO incluimos imágenes - se obtienen por separado
        dto.setCreatedAt(textItem.getCreatedAt());
        return dto;
    }

    /**
     * Convierte un DTO a entidad. Las imágenes se gestionan por separado.
     */
    private TextItem convertToEntity(TextItemDTO dto) {
        TextItem textItem = new TextItem();
        textItem.setId(dto.getId());
        textItem.setCategory(dto.getCategory());
        textItem.setTitle(dto.getTitle());
        textItem.setMessage(dto.getMessage());
        // No se maneja image como string
        return textItem;
    }
}
