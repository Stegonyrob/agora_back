package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PostTest {

  @Test
  public void testPost() {
    Post post;
    post =
      new Post(
        1L,
        "title",
        "message",
        LocalDateTime.now(),
        new User(),
        new Reply()
      );
    assertEquals(1L, post.getId());
    assertEquals("title", post.getTitle());
    assertEquals("message", post.getMessage());
    assertNotNull(post.getCreationDate());
    assertNotNull(post.getUser());
    assertNotNull(post.getReply());
    assertNotNull(post.getTags());

    List<Tag> tags = new ArrayList<>();
    tags.add(new Tag());
    post.setTags(tags);
    assertEquals(tags, post.getTags());
  }
}
