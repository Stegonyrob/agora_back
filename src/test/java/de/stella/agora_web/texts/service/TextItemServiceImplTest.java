package de.stella.agora_web.texts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.stella.agora_web.texts.controller.dto.TextItemDTO;
import de.stella.agora_web.texts.model.TextItem;
import de.stella.agora_web.texts.repository.TextItemRepository;
import de.stella.agora_web.texts.service.impl.TextItemServiceImpl;

class TextItemServiceImplTest {

    @Mock
    private TextItemRepository textItemRepository;

    @InjectMocks
    private TextItemServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateText() {
        TextItem item = new TextItem();
        item.setId(1L);
        item.setTitle("Old title");
        item.setDescription("Old desc");
        item.setImage("old.png");
        item.setNameImage("oldName");
        when(textItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(textItemRepository.save(any(TextItem.class))).thenReturn(item);

        TextItemDTO dto = new TextItemDTO();
        dto.setTitle("New title");
        dto.setDescription("New desc");
        dto.setImage("new.png");
        dto.setNameImage("newName");

        TextItemDTO result = service.updateText(1L, dto);
        assertEquals("New title", result.getTitle());
        assertEquals("New desc", result.getDescription());
        assertEquals("new.png", result.getImage());
        assertEquals("newName", result.getNameImage());
        verify(textItemRepository).save(any(TextItem.class));
    }

    @Test
    void testCreateText() {
        TextItem item = new TextItem();
        item.setId(2L);
        item.setTitle("Title");
        item.setDescription("Desc");
        item.setImage("img.png");
        item.setNameImage("nameImg");
        when(textItemRepository.save(any(TextItem.class))).thenReturn(item);

        TextItemDTO dto = new TextItemDTO();
        dto.setTitle("Title");
        dto.setDescription("Desc");
        dto.setImage("img.png");
        dto.setNameImage("nameImg");

        TextItemDTO result = service.createText(dto);
        assertEquals("Title", result.getTitle());
        assertEquals("Desc", result.getDescription());
        assertEquals("img.png", result.getImage());
        assertEquals("nameImg", result.getNameImage());
    }

    @Test
    void testGetTextByIdNotFound() {
        when(textItemRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getTextById(99L));
    }
}
