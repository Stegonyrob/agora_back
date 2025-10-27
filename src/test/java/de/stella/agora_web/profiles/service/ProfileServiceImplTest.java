package de.stella.agora_web.profiles.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.profiles.service.impl.ProfileServiceImpl;
import de.stella.agora_web.user.model.User;

class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProfileForUser() {
        User user = new User();
        user.setId(1L);
        ProfileDTO dto = mock(ProfileDTO.class);
        when(dto.getFirstName()).thenReturn("John");
        when(dto.getLastName1()).thenReturn("Doe");
        when(dto.getLastName2()).thenReturn("");
        when(dto.getUsername()).thenReturn("johndoe");
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setFirstName("John");
        profile.setLastName1("Doe");
        profile.setLastName2("");
        profile.setUsername("johndoe");
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Profile result = service.createProfileForUser(user, dto);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName1());
        assertEquals("johndoe", result.getUsername());
        assertEquals(1L, result.getUser().getId());
    }

    @Test
    void testFindProfileById() {
        Profile profile = new Profile();
        profile.setId(2L);
        when(profileRepository.findById(2L)).thenReturn(Optional.of(profile));

        Optional<Profile> result = service.findById(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
    }

    @Test
    void testDeleteProfile() {
        doNothing().when(profileRepository).deleteById(3L);
        String result = service.delete(3L);
        verify(profileRepository).deleteById(3L);
        assertEquals("Profile deleted", result);
    }
}
