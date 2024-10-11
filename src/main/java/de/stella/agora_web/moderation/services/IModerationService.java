package de.stella.agora_web.moderation.services;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.comment.model.Comment;

public interface IModerationService {
  CensuredComment moderateComment(Comment comment);
}
