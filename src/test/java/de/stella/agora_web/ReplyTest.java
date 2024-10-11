package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;

import de.stella.agora_web.replies.model.Reply;
import org.junit.jupiter.api.Test;

class ReplyTest {

  private Reply reply;

  @Test
  void testId() {
    assertEquals(1L, reply.getId());
  }

  @Test
  void testTitle() {
    assertEquals("Test title", reply.getTitle());
  }

  @Test
  void testMessage() {
    assertEquals("Test message", reply.getMessage());
  }

  @Test
  void testCreationDate() {
    assertNotNull(reply.getCreationDate());
  }

  @Test
  void testAuthor() {
    assertNotNull(reply.getUser());
  }

  @Test
  void testPosts() {
    assertNotNull(reply.getPost());
  }
}
