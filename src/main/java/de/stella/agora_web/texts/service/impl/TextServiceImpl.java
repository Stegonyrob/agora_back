package de.stella.agora_web.texts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.texts.controller.dto.TextDTO;
import de.stella.agora_web.texts.model.Text;
import de.stella.agora_web.texts.repository.TextRepository;
import de.stella.agora_web.texts.service.ITextService;

@Service
public class TextServiceImpl implements ITextService {

    @Autowired
    private TextRepository textRepository;

    @Override
    public List<TextDTO> getAllTexts() {
        return textRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TextDTO getTextById(Long id) {
        return textRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Text not found"));
    }

    @Override
    public TextDTO createText(TextDTO textDTO) {
        Text text = convertToEntity(textDTO);
        Text savedText = textRepository.save(text);
        return convertToDTO(savedText);
    }

    @Override
    public TextDTO updateText(Long id, TextDTO dto) {
        Text item = textRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text not found"));
        item.setTitle(dto.getTitle());
        item.setMessage(dto.getMessage());
        item.setCategory(dto.getCategory());
        item.setArchived(dto.isArchived());
        // No se maneja image como string, las imágenes se gestionan por separado
        Text updatedText = textRepository.save(item);
        return convertToDTO(updatedText);
    }

    @Override
    public void deleteText(Long id) {
        textRepository.deleteById(id);
    }

    @Override
    public void archiveText(Long id) {
        Text text = textRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text not found"));
        text.setArchived(true);
        textRepository.save(text);
    }

    @Override
    public void unArchiveText(Long id) {
        Text text = textRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text not found"));
        text.setArchived(false);
        textRepository.save(text);
    }

    /**
     * Convierte un Text a DTO SIN imágenes (patrón consistente con Posts y
     * Events). Las imágenes se obtienen por separado para eficiencia.
     */
    private TextDTO convertToDTO(Text text) {
        TextDTO dto = new TextDTO();
        dto.setId(text.getId());
        dto.setCategory(text.getCategory());
        dto.setTitle(text.getTitle());
        dto.setMessage(text.getMessage());
        dto.setArchived(text.isArchived());
        // NO incluimos imágenes - se obtienen por separado
        dto.setCreatedAt(text.getCreatedAt());
        return dto;
    }

    /**
     * Convierte un DTO a entidad. Las imágenes se gestionan por separado.
     */
    private Text convertToEntity(TextDTO dto) {
        Text textItem = new Text();
        textItem.setId(dto.getId());
        textItem.setCategory(dto.getCategory());
        textItem.setTitle(dto.getTitle());
        textItem.setMessage(dto.getMessage());
        textItem.setArchived(dto.isArchived());
        // No se maneja image como string
        return textItem;
    }
}
