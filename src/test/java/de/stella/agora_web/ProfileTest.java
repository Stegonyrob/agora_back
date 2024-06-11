package de.stella.agora_web;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.stella.agora_web.profiles.model.Profile;

public class ProfileTest {
    
    @Test
    public void testCreateProfile() {
        Profile profile = new Profile();
        assertNotNull(profile);
    }
    
    @Test
    public void testSetAndGetEmail() {
        Profile profile = new Profile();
        profile.setEmail("example@example.com");
        assertEquals("example@example.com", profile.getEmail());
    }
    
    //... otros tests para verificar las propiedades de la clase Profile...
}