package de.stella.agora_web.texts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.texts.controller.dto.TextItemDTO;
import de.stella.agora_web.texts.service.TextItemService;

@RestController
@RequestMapping(path = "${api-endpoint}/texts")

public class TextItemController {

    @Autowired
    private TextItemService textItemService;

    @GetMapping
    public List<TextItemDTO> getAllTexts() {
        return textItemService.getAllTexts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TextItemDTO> getTextById(@PathVariable Long id) {
        return ResponseEntity.ok(textItemService.getTextById(id));
    }

    @PostMapping
    public ResponseEntity<TextItemDTO> createText(@RequestBody TextItemDTO textItemDTO) {
        return ResponseEntity.ok(textItemService.createText(textItemDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TextItemDTO> updateText(@PathVariable Long id, @RequestBody TextItemDTO textItemDTO) {
        return ResponseEntity.ok(textItemService.updateText(id, textItemDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteText(@PathVariable Long id) {
        textItemService.deleteText(id);
        return ResponseEntity.noContent().build();
    }
}