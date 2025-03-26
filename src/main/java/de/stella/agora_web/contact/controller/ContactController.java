package de.stella.agora_web.contact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.contact.model.ContactForm;
import de.stella.agora_web.contact.service.ContactService;

@RestController
@RequestMapping("/api/v1/all/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<String> submitContactForm(@RequestBody ContactForm contactForm) {
        try {
            contactService.processContactForm(contactForm);
            return ResponseEntity.ok("Contact form submitted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit contact form.");
        }
    }
}
