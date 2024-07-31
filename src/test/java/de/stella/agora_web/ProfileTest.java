package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.stella.agora_web.profiles.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTest {

  @SuppressWarnings("unused")
  private Profile profile;

  private Profile testProfile;

  @BeforeEach
  public void setUp() {
    testProfile = new Profile();
  }

  @Test
  public void testIsFavorite() {
    assertEquals(false, testProfile.isFavorite());
  }

  @Test
  public void testSetFavorite() {
    testProfile.setFavorite(true);
    assertEquals(true, testProfile.isFavorite());
  }
}
