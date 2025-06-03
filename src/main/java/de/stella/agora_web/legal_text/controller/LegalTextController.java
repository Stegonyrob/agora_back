package de.stella.agora_web.legal_text.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.legal_text.controller.dto.LegalTextDTO;
import de.stella.agora_web.legal_text.service.ILegalTextService;

@RestController
@RequestMapping(path = "${api-endpoint}/legal")
public class LegalTextController {

    @Autowired
    private ILegalTextService service;

    @GetMapping
    public List<LegalTextDTO> getAllLegalTexts() {
        return service.findAll();
    }

    @GetMapping("/{type}")
    public LegalTextDTO getLegalText(@PathVariable String type) {
        return service.getByType(type);
    }

    @PutMapping("/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public LegalTextDTO updateLegalText(@PathVariable String type, @RequestBody LegalTextDTO dto) {
        return service.updateByType(type, dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LegalTextDTO createLegalText(@RequestBody LegalTextDTO dto) {
        // Implement logic to create a new legal text
        return service.create(dto);
    }

    @DeleteMapping("/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLegalText(@PathVariable String type) {
        // Implement logic to delete a legal text
        service.deleteByType(type);
        return ResponseEntity.noContent().build();
    }
}
