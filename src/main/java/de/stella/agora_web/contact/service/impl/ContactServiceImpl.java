package de.stella.agora_web.contact.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.stella.agora_web.contact.model.ContactForm;
import de.stella.agora_web.contact.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    private final List<ContactForm> contacts = new ArrayList<>();

    @Override
    public ContactForm createContact(ContactForm contact) {
        contact.setId((long) (contacts.size() + 1)); // Simulate ID generation
        contacts.add(contact);
        return contact;
    }

    @Override
    public void processContactForm(ContactForm contactForm) {
        // Implement the logic for processing the contact form
        System.out.println("Processing contact form: " + contactForm);
    }

    @Override
    public ContactForm getContactById(Long id) {
        Optional<ContactForm> contact = contacts.stream().filter(c -> c.getId().equals(id)).findFirst();
        return contact.orElse(null);
    }

    @Override
    public List<ContactForm> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    @Override
    public void deleteContact(Long id) {
        contacts.removeIf(contact -> contact.getId().equals(id));
    }

    @Override
    public ContactForm updateContact(Long id, ContactForm contact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId().equals(id)) {
                ContactForm updatedContact = new ContactForm();
                updatedContact.setId(id);
                contacts.set(i, updatedContact);
                return updatedContact;
            }
        }
        return null;
    }
}
