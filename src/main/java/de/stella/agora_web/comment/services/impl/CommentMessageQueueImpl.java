package de.stella.agora_web.comment.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.services.IMessageQueueService;

@Service
public class CommentMessageQueueImpl implements IMessageQueueService {
    private final List<Comment> queue = new ArrayList<>();

    @Override
    public void add(Comment comment) {
        queue.add(comment);
    }

    @Override
    public List<Comment> poll() {
        List<Comment> comments = new ArrayList<>(queue);
        queue.clear();
        return comments;
    }
}