package de.stella.agora_web.posts.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.image.controller.dto.PostImageDTO;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.exceptions.UserNotFoundException;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.IUserService;

/**
 * Comprehensive tests for PostServiceImpl - The largest service (935
 * instructions) Tests cover CRUD operations, tag management, comment/reply
 * operations, archiving, and pagination
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PostServiceImpl Unit Tests")
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private IUserService userService;

    @Mock
    private ITagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private User testUser;
    private Post testPost;
    private Tag testTag;
    private Comment testComment;
    private Reply testReply;
    private PostDTO postDTO;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test tag
        testTag = new Tag();
        testTag.setId(1L);
        testTag.setName("test-tag");

        // Setup test post
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setMessage("Test message #test-tag");
        testPost.setUser(testUser);
        testPost.setTags(new ArrayList<>(List.of(testTag)));
        testPost.setComments(new ArrayList<>());
        testPost.setImages(new ArrayList<>());
        testPost.setArchived(false);

        // Setup test comment
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setMessage("Test comment");
        testComment.setUser(testUser);
        testComment.setPost(testPost);
        testComment.setReplies(new ArrayList<>());
        testComment.setArchived(false);

        // Setup test reply
        testReply = new Reply();
        testReply.setId(1L);
        testReply.setMessage("Test reply");
        testReply.setUser(testUser);
        testReply.setComment(testComment);
        testReply.setArchived(false);

        // Setup PostDTO
        TagSummaryDTO tagDTO = new TagSummaryDTO();
        tagDTO.setId(1L);
        tagDTO.setName("test-tag");

        postDTO = new PostDTO();
        postDTO.setTitle("Test Post");
        postDTO.setMessage("Test message #test-tag");
        postDTO.setUserId(1L);
        postDTO.setTags(List.of(tagDTO));
    }

    // ============ TESTS GET ALL POSTS ============
    @Test
    @DisplayName("getAllPosts should return all posts")
    void testGetAllPosts_ShouldReturnAllPosts() {
        // Given
        List<Post> posts = Arrays.asList(testPost, new Post());
        when(postRepository.findAll()).thenReturn(posts);

        // When
        List<Post> result = postService.getAllPosts();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testPost);
        verify(postRepository).findAll();
    }

    @Test
    @DisplayName("getAllPosts should return empty list when no posts exist")
    void testGetAllPosts_EmptyList_ShouldReturnEmptyList() {
        // Given
        when(postRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Post> result = postService.getAllPosts();

        // Then
        assertThat(result).isEmpty();
        verify(postRepository).findAll();
    }

    // ============ TESTS GET POST BY ID ============
    @Test
    @DisplayName("getById should return post when found")
    void testGetById_ValidId_ShouldReturnPost() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        Post result = postService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Post");
        verify(postRepository).findById(1L);
    }

    @Test
    @DisplayName("getById should throw exception when post not found")
    void testGetById_InvalidId_ShouldThrowException() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.getById(999L))
                .isInstanceOf(NoSuchElementException.class);
        verify(postRepository).findById(999L);
    }

    // ============ TESTS CREATE POST ============
    @Test
    @DisplayName("createPost should create post successfully")
    void testCreatePost_ValidData_ShouldCreatePost() {
        // Given
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(tagService.getOrCreateTagByName("test-tag")).thenReturn(testTag);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        Post result = postService.createPost(postDTO, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Post");
        assertThat(result.getUser()).isEqualTo(testUser);
        verify(userService).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("createPost should throw UserNotFoundException when user not found")
    void testCreatePost_UserNotFound_ShouldThrowException() {
        // Given
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.createPost(postDTO, 999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: 999");
        verify(userService).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("createPost should handle posts with multiple tags")
    void testCreatePost_MultipleTags_ShouldCreateWithAllTags() {
        // Given
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("second-tag");

        TagSummaryDTO tagDTO2 = new TagSummaryDTO();
        tagDTO2.setId(2L);
        tagDTO2.setName("second-tag");

        postDTO.setTags(Arrays.asList(
                postDTO.getTags().get(0),
                tagDTO2
        ));

        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(tagService.getOrCreateTagByName("test-tag")).thenReturn(testTag);
        when(tagService.getOrCreateTagByName("second-tag")).thenReturn(tag2);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Post result = postService.createPost(postDTO, 1L);

        // Then
        assertThat(result.getTags()).hasSize(2);
        verify(tagService).getOrCreateTagByName("test-tag");
        verify(tagService).getOrCreateTagByName("second-tag");
    }

    // ============ TESTS UPDATE POST ============
    @Test
    @DisplayName("updatePost should update post successfully")
    void testUpdatePost_ValidData_ShouldUpdatePost() {
        // Given
        PostDTO updateDTO = new PostDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setMessage("Updated message");
        updateDTO.setUserId(1L);
        updateDTO.setTags(Collections.emptyList());

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        Post result = postService.updatePost(updateDTO, 1L);

        // Then
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getMessage()).isEqualTo("Updated message");
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("updatePost should throw exception when post not found")
    void testUpdatePost_PostNotFound_ShouldThrowException() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.updatePost(postDTO, 999L))
                .isInstanceOf(NoSuchElementException.class);
        verify(postRepository).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("updatePost should clear and add new tags")
    void testUpdatePost_WithNewTags_ShouldReplaceOldTags() {
        // Given
        Tag newTag = new Tag();
        newTag.setId(3L);
        newTag.setName("new-tag");

        TagSummaryDTO newTagDTO = new TagSummaryDTO();
        newTagDTO.setId(3L);
        newTagDTO.setName("new-tag");

        postDTO.setTags(List.of(newTagDTO));

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(tagService.getOrCreateTagByName("new-tag")).thenReturn(newTag);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Post result = postService.updatePost(postDTO, 1L);

        // Then
        assertThat(result.getTags()).hasSize(1);
        assertThat(result.getTags().get(0).getName()).isEqualTo("new-tag");
        verify(tagService).getOrCreateTagByName("new-tag");
    }

    // ============ TESTS ARCHIVE/UNARCHIVE POST ============
    @Test
    @DisplayName("archivePost should archive post and its comments and replies")
    void testArchivePost_ShouldArchivePostCommentsAndReplies() {
        // Given
        testComment.setReplies(List.of(testReply));
        testPost.setComments(List.of(testComment));

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        postService.archivePost(1L);

        // Then
        assertThat(testPost.isArchived()).isTrue();
        assertThat(testComment.getArchived()).isTrue();
        assertThat(testReply.getArchived()).isTrue();
        verify(postRepository).findById(1L);
        verify(postRepository).save(testPost);
    }

    @Test
    @DisplayName("archivePost should throw exception when post not found")
    void testArchivePost_PostNotFound_ShouldThrowException() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.archivePost(999L))
                .isInstanceOf(NoSuchElementException.class);
        verify(postRepository).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("unArchivePost should unarchive post and its comments and replies")
    void testUnArchivePost_ShouldUnArchivePostCommentsAndReplies() {
        // Given
        testPost.setArchived(true);
        testComment.setArchived(true);
        testReply.setArchived(true);
        testComment.setReplies(List.of(testReply));
        testPost.setComments(List.of(testComment));

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        postService.unArchivePost(1L);

        // Then
        assertThat(testPost.isArchived()).isFalse();
        assertThat(testComment.getArchived()).isFalse();
        assertThat(testReply.getArchived()).isFalse();
        verify(postRepository).findById(1L);
        verify(postRepository).save(testPost);
    }

    // ============ TESTS COMMENTS MANAGEMENT ============
    @Test
    @DisplayName("getCommentsByPostId should return all comments for a post")
    void testGetCommentsByPostId_ShouldReturnComments() {
        // Given
        testPost.setComments(Arrays.asList(testComment, new Comment()));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        List<Comment> result = postService.getCommentsByPostId(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testComment);
        verify(postRepository).findById(1L);
    }

    @Test
    @DisplayName("createComment should create comment successfully")
    void testCreateComment_ValidData_ShouldCreateComment() {
        // Given
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("New comment");
        commentDTO.setUserId(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        postService.createComment(1L, commentDTO);

        // Then
        verify(postRepository).findById(1L);
        verify(userService).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("createComment should throw exception when user not found")
    void testCreateComment_UserNotFound_ShouldThrowException() {
        // Given
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("New comment");
        commentDTO.setUserId(999L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.createComment(1L, commentDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: 999");
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("deleteComment should delete comment by id")
    void testDeleteComment_ValidId_ShouldDeleteComment() {
        // Given
        doNothing().when(commentRepository).deleteById(1L);

        // When
        postService.deleteComment(1L);

        // Then
        verify(commentRepository).deleteById(1L);
    }

    // ============ TESTS REPLIES MANAGEMENT ============
    @Test
    @DisplayName("getRepliesByCommentId should return all replies for a comment")
    void testGetRepliesByCommentId_ShouldReturnReplies() {
        // Given
        testComment.setReplies(Arrays.asList(testReply, new Reply()));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // When
        List<Reply> result = postService.getRepliesByCommentId(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testReply);
        verify(commentRepository).findById(1L);
    }

    @Test
    @DisplayName("createReply should create reply successfully")
    void testCreateReply_ValidData_ShouldCreateReply() {
        // Given
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("New reply");
        replyDTO.setUserId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(replyRepository.save(any(Reply.class))).thenReturn(testReply);

        // When
        postService.createReply(1L, replyDTO);

        // Then
        verify(commentRepository).findById(1L);
        verify(userService).findById(1L);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("createReply should throw exception when user not found")
    void testCreateReply_UserNotFound_ShouldThrowException() {
        // Given
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("New reply");
        replyDTO.setUserId(999L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.createReply(1L, replyDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: 999");
        verify(replyRepository, never()).save(any(Reply.class));
    }

    @Test
    @DisplayName("deleteReply should delete reply by id")
    void testDeleteReply_ValidId_ShouldDeleteReply() {
        // Given
        doNothing().when(replyRepository).deleteById(1L);

        // When
        postService.deleteReply(1L);

        // Then
        verify(replyRepository).deleteById(1L);
    }

    // ============ TESTS SAVE METHOD ============
    @Test
    @DisplayName("save should create post with tags and images")
    void testSave_WithTagsAndImages_ShouldCreatePost() {
        // Given
        PostImageDTO imageDTO = new PostImageDTO();
        imageDTO.setImageName("test-image.jpg");
        postDTO.setImages(List.of(imageDTO));

        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(tagService.getOrCreateTagByName("test-tag")).thenReturn(testTag);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Post result = postService.save(postDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Post");
        assertThat(result.getUser()).isEqualTo(testUser);
        verify(userService).findById(1L);
        verify(tagService).getOrCreateTagByName("test-tag");
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("save should throw UserNotFoundException when user not found")
    void testSave_UserNotFound_ShouldThrowException() {
        // Given
        postDTO.setUserId(999L);
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.save(postDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: 999");
        verify(postRepository, never()).save(any(Post.class));
    }

    // ============ EDGE CASES ============
    @Test
    @DisplayName("createPost should handle null tags list")
    void testCreatePost_NullTags_ShouldCreatePostWithoutTags() {
        // Given
        postDTO.setTags(null);
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Post result = postService.createPost(postDTO, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTags()).isEmpty();
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("archivePost with no comments should only archive post")
    void testArchivePost_NoComments_ShouldOnlyArchivePost() {
        // Given
        testPost.setComments(Collections.emptyList());
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        postService.archivePost(1L);

        // Then
        assertThat(testPost.isArchived()).isTrue();
        verify(postRepository).save(testPost);
    }

    @Test
    @DisplayName("getCommentsByPostId with no comments should return empty list")
    void testGetCommentsByPostId_NoComments_ShouldReturnEmptyList() {
        // Given
        testPost.setComments(Collections.emptyList());
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        List<Comment> result = postService.getCommentsByPostId(1L);

        // Then
        assertThat(result).isEmpty();
        verify(postRepository).findById(1L);
    }

    @Test
    @DisplayName("getRepliesByCommentId with no replies should return empty list")
    void testGetRepliesByCommentId_NoReplies_ShouldReturnEmptyList() {
        // Given
        testComment.setReplies(Collections.emptyList());
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // When
        List<Reply> result = postService.getRepliesByCommentId(1L);

        // Then
        assertThat(result).isEmpty();
        verify(commentRepository).findById(1L);
    }
}
