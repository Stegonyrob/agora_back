package de.stella.agora_web.replies.services;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.user.model.User;
import java.util.List;

public interface IReplyService {
  List<Reply> getAllReplys();

  Reply getReplyById(Long id);

  Reply createReply(@SuppressWarnings("rawtypes") ReplyDTO replyDTO, User user);

  @SuppressWarnings("rawtypes")
  Reply updateReply(Long id, ReplyDTO replyDTO);

  void deleteReply(Long id);

  Reply save(@SuppressWarnings("rawtypes") ReplyDTO replyDTO);
}
