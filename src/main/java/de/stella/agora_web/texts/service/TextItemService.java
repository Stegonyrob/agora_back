package de.stella.agora_web.texts.service;

import java.util.List;

import de.stella.agora_web.texts.controller.dto.TextItemDTO;

public interface TextItemService {
    List<TextItemDTO> getAllTexts();

    TextItemDTO getTextById(Long id);

    TextItemDTO createText(TextItemDTO textItemDTO);

    TextItemDTO updateText(Long id, TextItemDTO textItemDTO);

    void deleteText(Long id);
}