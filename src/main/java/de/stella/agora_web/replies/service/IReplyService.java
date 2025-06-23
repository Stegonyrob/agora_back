package de.stella.agora_web.replies.service;

import java.util.List;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.user.model.User;

public interface IReplyService {

    List<Reply> getAllReplies();

    Reply getReplyById(Long id);

    Reply createReply(ReplyDTO replyDTO, User user);

    Reply updateReply(Long id, ReplyDTO replyDTO);

    void deleteReply(Long id);

    List<Reply> getRepliesByCommentId(Long commentId);

    List<Reply> getRepliesByUserId(Long userId);

    List<Reply> getRepliesByTagName(String tagName);

    List<Reply> getRepliesByTagId(Long tagId);
}
