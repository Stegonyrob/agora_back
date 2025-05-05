package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.user.model.User;

class ReplyTest {

  private Reply reply;

  @BeforeEach
  void setup() {
    reply = new Reply();
    reply.setUser(new User());
  }

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
    assertNotNull(reply.getUser(), "Reply user should not be null");
  }

  @Test
  void testPosts() {
    assertNotNull(reply.getPost());
  }
}
