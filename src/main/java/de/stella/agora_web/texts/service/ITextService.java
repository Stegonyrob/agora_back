package de.stella.agora_web.texts.service;

import java.util.List;

import de.stella.agora_web.texts.controller.dto.TextDTO;

public interface ITextService {

    List<TextDTO> getAllTexts();

    TextDTO getTextById(Long id);

    List<TextDTO> getTextsByCategory(String category);

    TextDTO createText(TextDTO textDTO);

    TextDTO updateText(Long id, TextDTO textDTO);

    void deleteText(Long id);

    void archiveText(Long id);

    void unArchiveText(Long id);
}
