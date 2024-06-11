package de.stella.agora_web;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;

public class ReplyTest {

    @Test
    public void testReply() {
        User author = new User();
        Post post = new Post();
        Reply reply = new Reply("Test Title", "Test Message", author, post, true);

        Assertions.assertEquals("Test Title", reply.getTitle());
        Assertions.assertEquals("Test Message", reply.getMessage());
        Assertions.assertEquals(author, reply.getAuthor());
        Assertions.assertEquals(post, reply.getPost());
        Assertions.assertTrue(reply.isFavorite());
    }
}