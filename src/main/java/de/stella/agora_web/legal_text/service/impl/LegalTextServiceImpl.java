package de.stella.agora_web.legal_text.service.impl;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.legal_text.controller.dto.LegalTextDTO;
import de.stella.agora_web.legal_text.model.LegalText;
import de.stella.agora_web.legal_text.repository.LegalTextRepository;
import de.stella.agora_web.legal_text.service.ILegalTextService;

@Service
public class LegalTextServiceImpl implements ILegalTextService {

    private static final Logger logger = LoggerFactory.getLogger(LegalTextServiceImpl.class);
    private static final String NOT_FOUND_MESSAGE = "No existe el texto legal para: ";

    private final LegalTextRepository repository;

    public LegalTextServiceImpl(LegalTextRepository repository) {
        this.repository = repository;
    }

    @Override
    public LegalTextDTO getByType(String type) {
        LegalText entity = repository.findByType(type)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE + type));
        LegalTextDTO dto = new LegalTextDTO();
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        return dto;
    }

    @Override
    @Transactional
    public LegalTextDTO updateByType(String type, LegalTextDTO dto) {
        try {
            logger.info("Updating legal text for type: {}", type);
            logger.info("DTO received - title: {}, content length: {}",
                    dto.getTitle(), dto.getContent() != null ? dto.getContent().length() : "null");

            LegalText entity = repository.findByType(type)
                    .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE + type));

            logger.info("Found existing entity with ID: {}", entity.getId());

            // Validar datos antes de actualizar
            if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty");
            }
            if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }

            entity.setTitle(dto.getTitle().trim());
            entity.setContent(dto.getContent().trim());

            LegalText savedEntity = repository.save(entity);
            logger.info("Legal text saved successfully with ID: {}", savedEntity.getId());

            dto.setType(savedEntity.getType());
            return dto;
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new IllegalStateException("Error updating legal text for type " + type + ": " + e.getMessage(), e);
        }
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
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE + type));
        repository.delete(entity);
    }
}
