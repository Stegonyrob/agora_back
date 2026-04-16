package de.stella.agora_web.legal_text.service;

import java.util.List;

import de.stella.agora_web.legal_text.controller.dto.LegalTextDTO;

public interface ILegalTextService {

    LegalTextDTO getByType(String type);

    LegalTextDTO updateByType(String type, LegalTextDTO dto);

    List<LegalTextDTO> findAll();

    LegalTextDTO create(LegalTextDTO dto);

    void deleteByType(String type);
}