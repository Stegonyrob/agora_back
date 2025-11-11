package de.stella.agora_web.profiles.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.model.PostLove;
import de.stella.agora_web.posts.repository.PostLoveRepository;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.exceptions.ProfileNotFoundException;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.user.model.User;

/**
 * Comprehensive tests for ProfileServiceImpl (405 instructions) Tests cover
 * CRUD operations, favorites management, avatar handling, and authentication
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileServiceImpl Unit Tests")
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLoveRepository postLoveRepository;

    @Mock
    private AvatarRepository avatarRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private User testUser;
    private Profile testProfile;
    private ProfileDTO profileDTO;
    private Avatar testAvatar;
    private Post testPost;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test avatar
        testAvatar = new Avatar();
        testAvatar.setId(1L);
        testAvatar.setImageName("default-avatar.png");
        testAvatar.setDefault(true);

        // Setup test profile
        testProfile = new Profile();
        testProfile.setId(1L);
        testProfile.setUser(testUser);
        testProfile.setFirstName("John");
        testProfile.setLastName1("Doe");
        testProfile.setLastName2("Smith");
        testProfile.setUsername("testuser");
        testProfile.setEmail("test@example.com");
        testProfile.setCity("Madrid");
        testProfile.setCountry("Spain");
        testProfile.setPhone("123456789");
        testProfile.setRelationship("Single");
        testProfile.setAvatar(testAvatar);

        // Setup ProfileDTO
        profileDTO = new ProfileDTO();
        profileDTO.setFirstName("John");
        profileDTO.setLastName1("Doe");
        profileDTO.setLastName2("Smith");
        profileDTO.setUsername("testuser");
        profileDTO.setEmail("test@example.com");
        profileDTO.setCity("Madrid");
        profileDTO.setCountry("Spain");
        profileDTO.setPhone("123456789");
        profileDTO.setRelationship("Single");
        profileDTO.setPassword("password123");
        profileDTO.setConfirmPassword("password123");
        profileDTO.setAvatarId(1L);

        // Setup test post
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setUser(testUser);
    }

    // ============ TESTS CREATE PROFILE ============
    @Test
    @DisplayName("createProfileForUser should create profile with avatar")
    void testCreateProfileForUser_WithAvatar_ShouldCreateProfile() {
        // Given
        when(avatarRepository.findById(1L)).thenReturn(Optional.of(testAvatar));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        Profile result = profileService.createProfileForUser(testUser, profileDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getAvatar()).isNotNull();
        assertThat(result.getAvatar().getId()).isEqualTo(1L);
        verify(avatarRepository).findById(1L);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("createProfileForUser should use default avatar when no avatarId provided")
    void testCreateProfileForUser_NoAvatarId_ShouldUseDefaultAvatar() {
        // Given
        profileDTO.setAvatarId(null);
        when(avatarRepository.findDefaultAvatar()).thenReturn(Optional.of(testAvatar));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        Profile result = profileService.createProfileForUser(testUser, profileDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAvatar()).isNotNull();
        verify(avatarRepository).findDefaultAvatar();
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("createProfileForUser should throw exception when avatar not found")
    void testCreateProfileForUser_AvatarNotFound_ShouldThrowException() {
        // Given
        when(avatarRepository.findById(999L)).thenReturn(Optional.empty());
        profileDTO.setAvatarId(999L);

        // When & Then
        assertThatThrownBy(() -> profileService.createProfileForUser(testUser, profileDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Avatar no encontrado con ID: 999");
        verify(avatarRepository).findById(999L);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    // ============ TESTS GET BY ID ============
    @Test
    @DisplayName("getById should return profile with user and roles")
    void testGetById_ValidId_ShouldReturnProfile() {
        // Given
        when(profileRepository.findByIdWithUserAndRoles(1L)).thenReturn(Optional.of(testProfile));

        // When
        Profile result = profileService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(profileRepository).findByIdWithUserAndRoles(1L);
    }

    @Test
    @DisplayName("getById should throw ProfileNotFoundException when not found")
    void testGetById_InvalidId_ShouldThrowException() {
        // Given
        when(profileRepository.findByIdWithUserAndRoles(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.getById(999L))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessageContaining("Profile not found");
        verify(profileRepository).findByIdWithUserAndRoles(999L);
    }

    // ============ TESTS GET BY EMAIL ============
    @Test
    @DisplayName("getByEmail should return profile when found")
    void testGetByEmail_ValidEmail_ShouldReturnProfile() {
        // Given
        when(profileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testProfile));

        // When
        Profile result = profileService.getByEmail("test@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(profileRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("getByEmail should throw ProfileNotFoundException when not found")
    void testGetByEmail_InvalidEmail_ShouldThrowException() {
        // Given
        when(profileRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.getByEmail("invalid@example.com"))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessageContaining("Profile not found");
        verify(profileRepository).findByEmail("invalid@example.com");
    }

    // ============ TESTS UPDATE PROFILE ============
    @Test
    @DisplayName("updateProfile should update profile successfully")
    void testUpdateProfile_ValidData_ShouldUpdateProfile() {
        // Given
        ProfileDTO updateDTO = new ProfileDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setLastName1("Updated");
        updateDTO.setCity("Barcelona");
        updateDTO.setCountry("Spain");
        updateDTO.setPhone("987654321");
        updateDTO.setRelationship("Married");
        updateDTO.setAvatarId(1L);

        Profile updatedProfile = new Profile();
        updatedProfile.setId(1L);
        updatedProfile.setFirstName("Jane");
        updatedProfile.setLastName1("Updated");
        updatedProfile.setCity("Barcelona");

        when(profileRepository.findByIdWithUser(1L)).thenReturn(Optional.of(testProfile));
        when(avatarRepository.findById(1L)).thenReturn(Optional.of(testAvatar));
        when(profileRepository.save(any(Profile.class))).thenReturn(updatedProfile);

        // When
        Profile result = profileService.updateProfile(updateDTO, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(profileRepository).findByIdWithUser(1L);
        verify(avatarRepository).findById(1L);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("updateProfile should use default avatar when avatarId is null and profile has no avatar")
    void testUpdateProfile_NoAvatarId_ShouldUseDefaultAvatar() {
        // Given
        profileDTO.setAvatarId(null);
        testProfile.setAvatar(null);

        when(profileRepository.findByIdWithUser(1L)).thenReturn(Optional.of(testProfile));
        when(avatarRepository.findDefaultAvatar()).thenReturn(Optional.of(testAvatar));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        Profile result = profileService.updateProfile(profileDTO, 1L);

        // Then
        assertThat(result).isNotNull();
        verify(avatarRepository).findDefaultAvatar();
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("updateProfile should throw exception when profile not found")
    void testUpdateProfile_ProfileNotFound_ShouldThrowException() {
        // Given
        when(profileRepository.findByIdWithUser(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.updateProfile(profileDTO, 999L))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessageContaining("Profile Not Found");
        verify(profileRepository).findByIdWithUser(999L);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    // ============ TESTS FIND ALL PROFILES ============
    @Test
    @DisplayName("findAllProfiles should return all profiles")
    void testFindAllProfiles_ShouldReturnAllProfiles() {
        // Given
        Profile profile2 = new Profile();
        profile2.setId(2L);
        profile2.setFirstName("Jane");

        List<Profile> profiles = Arrays.asList(testProfile, profile2);
        when(profileRepository.findAll()).thenReturn(profiles);

        // When
        List<Profile> result = profileService.findAllProfiles();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(1).getFirstName()).isEqualTo("Jane");
        verify(profileRepository).findAll();
    }

    // ============ TESTS FIND BY ID (Optional) ============
    @Test
    @DisplayName("findById should return Optional with profile when found")
    void testFindById_ValidId_ShouldReturnOptional() {
        // Given
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));

        // When
        Optional<Profile> result = profileService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(profileRepository).findById(1L);
    }

    @Test
    @DisplayName("findById should return empty Optional when not found")
    void testFindById_InvalidId_ShouldReturnEmpty() {
        // Given
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Profile> result = profileService.findById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(profileRepository).findById(999L);
    }

    // ============ TESTS ADD FAVORITE POST ============
    @Test
    @DisplayName("addFavoritePost should add post to favorites")
    void testAddFavoritePost_NotExists_ShouldAddToFavorites() {
        // Given
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postLoveRepository.existsByProfileAndPost(testProfile, testPost)).thenReturn(false);
        when(postLoveRepository.save(any(PostLove.class))).thenReturn(new PostLove());

        // When
        String result = profileService.addFavoritePost(1L, 1L);

        // Then
        assertThat(result).isEqualTo("Post is added to favorites");
        verify(profileRepository).findById(1L);
        verify(postRepository).findById(1L);
        verify(postLoveRepository).existsByProfileAndPost(testProfile, testPost);
        verify(postLoveRepository).save(any(PostLove.class));
    }

    @Test
    @DisplayName("addFavoritePost should return message when post already in favorites")
    void testAddFavoritePost_AlreadyExists_ShouldReturnMessage() {
        // Given
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postLoveRepository.existsByProfileAndPost(testProfile, testPost)).thenReturn(true);

        // When
        String result = profileService.addFavoritePost(1L, 1L);

        // Then
        assertThat(result).isEqualTo("Post is already in favorites");
        verify(postLoveRepository, never()).save(any(PostLove.class));
    }

    @Test
    @DisplayName("addFavoritePost should throw exception when profile not found")
    void testAddFavoritePost_ProfileNotFound_ShouldThrowException() {
        // Given
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.addFavoritePost(999L, 1L))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessageContaining("Profile not found with ID: 999");
        verify(profileRepository).findById(999L);
        verify(postLoveRepository, never()).save(any(PostLove.class));
    }

    @Test
    @DisplayName("addFavoritePost should throw exception when post not found")
    void testAddFavoritePost_PostNotFound_ShouldThrowException() {
        // Given
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.addFavoritePost(1L, 999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found with ID: 999");
        verify(postRepository).findById(999L);
        verify(postLoveRepository, never()).save(any(PostLove.class));
    }

    // ============ TESTS DELETE FAVORITE POST ============
    @Test
    @DisplayName("deleteFavoritePost should remove post from favorites")
    void testDeleteFavoritePost_Exists_ShouldRemoveFromFavorites() {
        // Given
        PostLove postLove = new PostLove();
        postLove.setProfile(testProfile);
        postLove.setPost(testPost);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postLoveRepository.findByProfileAndPost(testProfile, testPost)).thenReturn(Optional.of(postLove));
        doNothing().when(postLoveRepository).delete(postLove);

        // When
        String result = profileService.deleteFavoritePost(1L, 1L);

        // Then
        assertThat(result).isEqualTo("Post is removed from favorites");
        verify(profileRepository).findById(1L);
        verify(postRepository).findById(1L);
        verify(postLoveRepository).findByProfileAndPost(testProfile, testPost);
        verify(postLoveRepository).delete(postLove);
    }

    @Test
    @DisplayName("deleteFavoritePost should return message when post not in favorites")
    void testDeleteFavoritePost_NotExists_ShouldReturnMessage() {
        // Given
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postLoveRepository.findByProfileAndPost(testProfile, testPost)).thenReturn(Optional.empty());

        // When
        String result = profileService.deleteFavoritePost(1L, 1L);

        // Then
        assertThat(result).isEqualTo("Post not found in favorites");
        verify(postLoveRepository, never()).delete(any(PostLove.class));
    }

    // ============ TESTS UPDATE FAVORITES (TOGGLE) ============
    @Test
    @DisplayName("updateFavorites should add post when not in favorites")
    void testUpdateFavorites_NotExists_ShouldAddToFavorites() {
        // Given
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(profileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postLoveRepository.findByProfileAndPost(testProfile, testPost)).thenReturn(Optional.empty());
        when(postLoveRepository.save(any(PostLove.class))).thenReturn(new PostLove());

        // When
        String result = profileService.updateFavorites(1L);

        // Then
        assertThat(result).isEqualTo("Post is added to favorites");
        verify(profileRepository).findByEmail("test@example.com");
        verify(postLoveRepository).save(any(PostLove.class));
    }

    @Test
    @DisplayName("updateFavorites should remove post when already in favorites")
    void testUpdateFavorites_Exists_ShouldRemoveFromFavorites() {
        // Given
        PostLove postLove = new PostLove();
        postLove.setProfile(testProfile);
        postLove.setPost(testPost);

        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(profileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testProfile));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postLoveRepository.findByProfileAndPost(testProfile, testPost)).thenReturn(Optional.of(postLove));
        doNothing().when(postLoveRepository).delete(postLove);

        // When
        String result = profileService.updateFavorites(1L);

        // Then
        assertThat(result).isEqualTo("Post is removed from favorites");
        verify(postLoveRepository).delete(postLove);
        verify(postLoveRepository, never()).save(any(PostLove.class));
    }

    // ============ TESTS GET FAVORITES ============
    @Test
    @DisplayName("getFavorites should return profile with favorites")
    void testGetFavorites_ValidId_ShouldReturnProfile() {
        // Given
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));

        // When
        Profile result = profileService.getFavorites(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(profileRepository).findById(1L);
    }

    @Test
    @DisplayName("getFavorites should throw exception when profile not found")
    void testGetFavorites_ProfileNotFound_ShouldThrowException() {
        // Given
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.getFavorites(999L))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessageContaining("Profile not found with ID: 999");
        verify(profileRepository).findById(999L);
    }

    // ============ TESTS DELETE PROFILE ============
    @Test
    @DisplayName("delete should delete profile successfully")
    void testDelete_ValidId_ShouldDeleteProfile() {
        // Given
        doNothing().when(profileRepository).deleteById(1L);

        // When
        String result = profileService.delete(1L);

        // Then
        assertThat(result).isEqualTo("Profile deleted");
        verify(profileRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete should throw NullPointerException when id is null")
    void testDelete_NullId_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> profileService.delete(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Profile ID cannot be null");
        verify(profileRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("delete should throw RuntimeException when deletion fails")
    void testDelete_DeletionFails_ShouldThrowException() {
        // Given
        doThrow(new RuntimeException("Database error")).when(profileRepository).deleteById(1L);

        // When & Then
        assertThatThrownBy(() -> profileService.delete(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error deleting profile with ID: 1");
        verify(profileRepository).deleteById(1L);
    }

    // ============ TESTS UPDATE METHOD (ALIAS) ============
    @Test
    @DisplayName("update should delegate to updateProfile")
    void testUpdate_ShouldDelegateToUpdateProfile() {
        // Given
        when(profileRepository.findByIdWithUser(1L)).thenReturn(Optional.of(testProfile));
        when(avatarRepository.findById(1L)).thenReturn(Optional.of(testAvatar));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        Profile result = profileService.update(profileDTO, 1L);

        // Then
        assertThat(result).isNotNull();
        verify(profileRepository).findByIdWithUser(1L);
        verify(profileRepository).save(any(Profile.class));
    }

    // ============ EDGE CASES ============
    @Test
    @DisplayName("updateProfile should keep current avatar when avatarId is null and profile has avatar")
    void testUpdateProfile_NullAvatarId_ShouldKeepCurrentAvatar() {
        // Given
        profileDTO.setAvatarId(null);
        testProfile.setAvatar(testAvatar); // Profile already has avatar

        when(profileRepository.findByIdWithUser(1L)).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        Profile result = profileService.updateProfile(profileDTO, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAvatar()).isNotNull();
        verify(avatarRepository, never()).findById(anyLong());
        verify(avatarRepository, never()).findDefaultAvatar();
    }

    @Test
    @DisplayName("updateFavorites should throw exception when profile not found by email")
    void testUpdateFavorites_ProfileNotFound_ShouldThrowException() {
        // Given
        when(authentication.getName()).thenReturn("invalid@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(profileRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> profileService.updateFavorites(1L))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessageContaining("Profile not found");
        verify(profileRepository).findByEmail("invalid@example.com");
    }
}
