package de.stella.agora_web.legal_text.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.legal_text.controller.dto.LegalTextDTO;
import de.stella.agora_web.legal_text.model.LegalText;
import de.stella.agora_web.legal_text.repository.LegalTextRepository;
import de.stella.agora_web.legal_text.service.ILegalTextService;

@Service
public class LegalTextServiceImpl implements ILegalTextService {

    @Autowired
    private LegalTextRepository repository;

    @Override
    public LegalTextDTO getByType(String type) {
        LegalText entity = repository.findByType(type)
                .orElseThrow(() -> new NoSuchElementException("No existe el texto legal para: " + type));
        LegalTextDTO dto = new LegalTextDTO();
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        return dto;
    }

    @Override
    public LegalTextDTO updateByType(String type, LegalTextDTO dto) {
        LegalText entity = repository.findByType(type)
                .orElseThrow(() -> new NoSuchElementException("No existe el texto legal para: " + type));
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        repository.save(entity);
        dto.setType(entity.getType());
        return dto;
    }

    @Override
    public LegalTextDTO create(LegalTextDTO dto) {
        LegalText entity = new LegalText();
        entity.setType(dto.getType());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        repository.save(entity);
        return dto;
    }

    @Override
    public java.util.List<LegalTextDTO> findAll() {
        java.util.List<LegalTextDTO> dtos = new java.util.ArrayList<>();
        for (LegalText entity : repository.findAll()) {
            LegalTextDTO dto = new LegalTextDTO();
            dto.setType(entity.getType());
            dto.setTitle(entity.getTitle());
            dto.setContent(entity.getContent());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public void deleteByType(String type) {
        LegalText entity = repository.findByType(type)
                .orElseThrow(() -> new NoSuchElementException("No existe el texto legal para: " + type));
        repository.delete(entity);
    }
}