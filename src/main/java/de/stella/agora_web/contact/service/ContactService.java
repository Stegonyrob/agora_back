package de.stella.agora_web.contact.service;

import java.util.List;

import de.stella.agora_web.contact.model.ContactForm;

public interface ContactService {
    ContactForm createContact(ContactForm contact);

    ContactForm getContactById(Long id);

    List<ContactForm> getAllContacts();

    ContactForm updateContact(Long id, ContactForm contact);

    void deleteContact(Long id);

    void processContactForm(ContactForm contactForm);
}