package de.stella.agora_web.comment.services;

import java.util.List;

import de.stella.agora_web.comment.model.Comment;

public interface IMessageQueueService {
  void add(Comment comment);

  List<Comment> poll();
}