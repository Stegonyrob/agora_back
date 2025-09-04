package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import de.stella.agora_web.image.service.ITextImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/text-images")
@RequiredArgsConstructor
public class TextImageController {

    private final ITextImageService textImageService;

    @GetMapping("/text/{textId}")
    public ResponseEntity<List<TextImageDTO>> getImagesByText(@PathVariable Long textId) {
        return ResponseEntity.ok(textImageService.getImagesByTextId(textId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TextImageDTO> getTextImage(@PathVariable Long id) {
        return ResponseEntity.ok(textImageService.getTextImageById(id));
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<byte[]> getTextImageData(@PathVariable Long id) {
        byte[] imageData = textImageService.getTextImageData(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageData.length);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TextImageDTO> uploadTextImage(@RequestBody TextImageDTO dto) {
        return ResponseEntity.ok(textImageService.createTextImage(dto));
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TextImageDTO>> uploadMultipleTextImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("textId") Long textId) {
        List<TextImageDTO> savedImages = textImageService.processAndSaveImages(files, textId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTextImage(@PathVariable Long id) {
        textImageService.deleteTextImage(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMultipleTextImages(@RequestBody List<Long> imageIds) {
        textImageService.deleteMultipleTextImages(imageIds);
        return ResponseEntity.noContent().build();
    }
}
