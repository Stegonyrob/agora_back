package de.stella.agora_web.posts;
// package de.stella.agora_web;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import org.junit.jupiter.api.Test;
// import de.stella.agora_web.posts.model.Post;
// import de.stella.agora_web.tags.model.Tag;
// public class PostTest {
// @Test
// public void testPostConstructorAndDefaults() {
// Post post = new Post();
// assertNotNull(post.getId(), "Post ID should not be null");
// assertNotNull(post.getTitle(), "Post title should not be null");
// assertNotNull(post.getContent(), "Post content should not be null");
// }
// @Test
// public void testSetUserId() {
// Post post = new Post();
// post.setUserId(123L);
// assertNotNull(post.getUser());
// assertEquals(123L, post.getUser().getId());
// }
// @Test
// public void testSetAndGetTags() {
// Post post = new Post();
// List<Tag> tags = new ArrayList<>();
// Tag tag1 = new Tag();
// Tag tag2 = new Tag();
// tags.add(tag1);
// tags.add(tag2);
// post.setTags(tags);
// assertEquals(tags, post.getTags());
// }
// @Test
// public void testToString() {
// Post post = new Post(1L, "Test Title", "Test Message", 123L, false, "Test
// User");
// post.setCreationDate(LocalDateTime.of(2023, 1, 1, 12, 0)); // Set a fixed
// creation date
// String expected = "Post{id=1, title='Test Title', message='Test Message',
// creationDate=2023-01-01T12:00, archived=false, user=123, comments=null,
// tags=null}";
// assertEquals(expected, post.toString());
// }
// @Test
// public void testGetLoves() {
// Post post = new Post();
// assertEquals(0, post.getLoves());
// }

// }
