package de.stella.agora_web.texts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.texts.controller.dto.TextItemDTO;
import de.stella.agora_web.texts.model.TextItem;
import de.stella.agora_web.texts.repository.TextItemRepository;
import de.stella.agora_web.texts.service.TextItemService;

@Service
public class TextItemServiceImpl implements TextItemService {

    @Autowired
    private TextItemRepository textItemRepository;

    @Override
    public List<TextItemDTO> getAllTexts() {
        return textItemRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TextItemDTO getTextById(Long id) {
        return textItemRepository.findById(id).map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Text not found"));
    }

    @Override
    public TextItemDTO createText(TextItemDTO textItemDTO) {
        TextItem textItem = convertToEntity(textItemDTO);
        TextItem savedText = textItemRepository.save(textItem);
        return convertToDTO(savedText);
    }

    @Override
    public TextItemDTO updateText(Long id, TextItemDTO textItemDTO) {
        TextItem textItem = textItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Text not found"));

        textItem.setImage(textItemDTO.getImage());
        textItem.setDescription(textItemDTO.getDescription());

        TextItem updatedText = textItemRepository.save(textItem);
        return convertToDTO(updatedText);
    }

    @Override
    public void deleteText(Long id) {
        textItemRepository.deleteById(id);
    }

    private TextItemDTO convertToDTO(TextItem textItem) {
        TextItemDTO dto = new TextItemDTO();
        dto.setId(textItem.getId());
        dto.setNameImage(textItem.getNameImage());
        dto.setTitle(textItem.getTitle());
        dto.setImage(textItem.getImage());
        dto.setDescription(textItem.getDescription());
        return dto;
    }

    private TextItem convertToEntity(TextItemDTO dto) {
        TextItem textItem = new TextItem(null, null, null, null, null);
        textItem.setId(dto.getId());
        textItem.setImage(dto.getImage());
        textItem.setTitle(dto.getTitle());
        textItem.setNameImage(dto.getNameImage);
        textItem.setDescription(dto.getDescription());
        return textItem;
    }
}