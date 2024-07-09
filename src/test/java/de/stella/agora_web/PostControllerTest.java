package de.stella.agora_web;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.stella.agora_web.posts.controller.PostController;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.services.IPostService;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPostService postService;

    @Test
    public void testGetAllPosts() throws Exception {
        // given
        Mockito.when(postService.getAllPosts()).thenReturn(List.of(new Post()));

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/posts").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

}
