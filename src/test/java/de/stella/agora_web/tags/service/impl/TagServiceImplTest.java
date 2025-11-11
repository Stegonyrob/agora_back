package de.stella.agora_web.tags.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.tags.dto.TagListDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Tests unitarios para TagServiceImpl Cobertura completa de métodos de servicio
 * de tags
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TagServiceImpl Tests")
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag testTag;
    private Post testPost;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testTag = createTag(1L, "java");
        testPost = createPost(1L, "Test Post");
        testEvent = createEvent(1L, "Test Event");
    }

    // ========== Tests para getAllTags() ==========
    @Test
    @DisplayName("getAllTags debe retornar lista de todos los tags")
    void shouldReturnAllTags() {
        // Arrange
        List<Tag> tags = Arrays.asList(
                createTag(1L, "java"),
                createTag(2L, "spring"),
                createTag(3L, "testing")
        );
        when(tagRepository.findAll()).thenReturn(tags);

        // Act
        List<Tag> result = tagService.getAllTags();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllTags debe retornar lista vacía cuando no hay tags")
    void shouldReturnEmptyListWhenNoTags() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Tag> result = tagService.getAllTags();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tagRepository, times(1)).findAll();
    }

    // ========== Tests para getTagById() ==========
    @Test
    @DisplayName("getTagById debe retornar tag cuando existe")
    void shouldReturnTagWhenExists() {
        // Arrange
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        // Act
        Tag result = tagService.getTagById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("java", result.getName());
        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getTagById debe retornar null cuando no existe")
    void shouldReturnNullWhenTagNotExists() {
        // Arrange
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Tag result = tagService.getTagById(99L);

        // Assert
        assertNull(result);
        verify(tagRepository, times(1)).findById(99L);
    }

    // ========== Tests para getOrCreateTagByName() ==========
    @Test
    @DisplayName("getOrCreateTagByName debe retornar tag existente")
    void shouldReturnExistingTag() {
        // Arrange
        when(tagRepository.findByName("java")).thenReturn(Arrays.asList(testTag));

        // Act
        Tag result = tagService.getOrCreateTagByName("java");

        // Assert
        assertNotNull(result);
        assertEquals("java", result.getName());
        verify(tagRepository, times(1)).findByName("java");
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    @DisplayName("getOrCreateTagByName debe crear nuevo tag cuando no existe")
    void shouldCreateNewTagWhenNotExists() {
        // Arrange
        when(tagRepository.findByName("kotlin")).thenReturn(new ArrayList<>());
        Tag newTag = createTag(null, "kotlin");
        when(tagRepository.save(any(Tag.class))).thenReturn(createTag(4L, "kotlin"));

        // Act
        Tag result = tagService.getOrCreateTagByName("kotlin");

        // Assert
        assertNotNull(result);
        verify(tagRepository, times(1)).findByName("kotlin");
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    @DisplayName("getOrCreateTagByName debe retornar null con nombre nulo")
    void shouldReturnNullWithNullName() {
        // Act
        Tag result = tagService.getOrCreateTagByName(null);

        // Assert
        assertNull(result);
        verify(tagRepository, never()).findByName(anyString());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    @DisplayName("getOrCreateTagByName debe retornar null con nombre vacío")
    void shouldReturnNullWithBlankName() {
        // Act
        Tag result = tagService.getOrCreateTagByName("   ");

        // Assert
        assertNull(result);
        verify(tagRepository, never()).findByName(anyString());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    // ========== Tests para getTagByName() ==========
    @Test
    @DisplayName("getTagByName debe retornar tag cuando existe")
    void shouldReturnTagByName() {
        // Arrange
        when(tagRepository.findByName("java")).thenReturn(Arrays.asList(testTag));

        // Act
        Tag result = tagService.getTagByName("java");

        // Assert
        assertNotNull(result);
        assertEquals("java", result.getName());
        verify(tagRepository, times(1)).findByName("java");
    }

    @Test
    @DisplayName("getTagByName debe retornar null cuando no existe")
    void shouldReturnNullWhenTagByNameNotExists() {
        // Arrange
        when(tagRepository.findByName("rust")).thenReturn(new ArrayList<>());

        // Act
        Tag result = tagService.getTagByName("rust");

        // Assert
        assertNull(result);
        verify(tagRepository, times(1)).findByName("rust");
    }

    // ========== Tests para extractHashtags() ==========
    @Test
    @DisplayName("extractHashtags debe extraer hashtags correctamente")
    void shouldExtractHashtags() {
        // Arrange
        String text = "Este es un #test de #hashtags para #java y #spring";

        // Act
        List<String> result = tagService.extractHashtags(text);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("test"));
        assertTrue(result.contains("hashtags"));
        assertTrue(result.contains("java"));
        assertTrue(result.contains("spring"));
    }

    @Test
    @DisplayName("extractHashtags debe retornar lista vacía con texto nulo")
    void shouldReturnEmptyListWithNullText() {
        // Act
        List<String> result = tagService.extractHashtags(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("extractHashtags debe retornar lista vacía sin hashtags")
    void shouldReturnEmptyListWithoutHashtags() {
        // Arrange
        String text = "Este texto no tiene hashtags";

        // Act
        List<String> result = tagService.extractHashtags(text);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("extractHashtags debe manejar hashtags duplicados")
    void shouldHandleDuplicateHashtags() {
        // Arrange
        String text = "#java es #java y siempre será #java";

        // Act
        List<String> result = tagService.extractHashtags(text);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Permite duplicados
    }

    @Test
    @DisplayName("extractHashtags debe manejar hashtags con números")
    void shouldHandleHashtagsWithNumbers() {
        // Arrange
        String text = "#java21 #spring3 #test123";

        // Act
        List<String> result = tagService.extractHashtags(text);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("java21"));
        assertTrue(result.contains("spring3"));
        assertTrue(result.contains("test123"));
    }

    // ========== Tests para createTagsBulk() ==========
    @Test
    @DisplayName("createTagsBulk debe crear múltiples tags")
    void shouldCreateMultipleTags() {
        // Arrange
        TagListDTO tagListDTO = new TagListDTO();
        List<TagSummaryDTO> tagDTOs = Arrays.asList(
                createTagDTO("java"),
                createTagDTO("spring"),
                createTagDTO("testing")
        );
        tagListDTO.setTags(tagDTOs);

        when(tagRepository.findByName(anyString())).thenReturn(new ArrayList<>());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(1L);
            return tag;
        });

        // Act
        List<Tag> result = tagService.createTagsBulk(tagListDTO);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tagRepository, times(3)).save(any(Tag.class));
    }

    @Test
    @DisplayName("createTagsBulk debe retornar lista vacía con DTO nulo")
    void shouldReturnEmptyListWithNullDTO() {
        // Act
        List<Tag> result = tagService.createTagsBulk(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    @DisplayName("createTagsBulk debe omitir tags con nombre vacío")
    void shouldSkipEmptyTagNames() {
        // Arrange
        TagListDTO tagListDTO = new TagListDTO();
        List<TagSummaryDTO> tagDTOs = Arrays.asList(
                createTagDTO("java"),
                createTagDTO(""),
                createTagDTO("   "),
                createTagDTO("spring")
        );
        tagListDTO.setTags(tagDTOs);

        when(tagRepository.findByName(anyString())).thenReturn(new ArrayList<>());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(1L);
            return tag;
        });

        // Act
        List<Tag> result = tagService.createTagsBulk(tagListDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Solo java y spring
        verify(tagRepository, times(2)).save(any(Tag.class));
    }

    // ========== Tests para addTagToPost() ==========
    @Test
    @DisplayName("addTagToPost debe lanzar EntityNotFoundException cuando post no existe")
    void shouldThrowExceptionWhenPostNotExists() {
        // Arrange
        when(postRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            tagService.addTagToPost(99L, "java");
        });
    }

    // ========== Tests para getTagsByPostId() ==========
    @Test
    @DisplayName("getTagsByPostId debe lanzar EntityNotFoundException cuando post no existe")
    void shouldThrowExceptionWhenGettingTagsForNonExistentPost() {
        // Arrange
        when(postRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            tagService.getTagsByPostId(99L);
        });
    }

    // ========== Tests para getTagsByEventId() ==========
    @Test
    @DisplayName("getTagsByEventId debe lanzar EntityNotFoundException cuando event no existe")
    void shouldThrowExceptionWhenGettingTagsForNonExistentEvent() {
        // Arrange
        when(eventRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            tagService.getTagsByEventId(99L);
        });
    }

    // ========== Métodos Helper ==========
    private Tag createTag(Long id, String name) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        return tag;
    }

    private Post createPost(Long id, String title) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        return post;
    }

    private Event createEvent(Long id, String title) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        return event;
    }

    private TagSummaryDTO createTagDTO(String name) {
        TagSummaryDTO dto = new TagSummaryDTO();
        dto.setName(name);
        return dto;
    }
}
