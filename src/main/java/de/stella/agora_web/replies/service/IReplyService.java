package de.stella.agora_web.replies.service;

import java.util.List;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.user.model.User;

public interface IReplyService {

    List<Reply> getAllReplys();

    Reply getReplyById(Long id);

    Reply createReply(@SuppressWarnings("rawtypes") ReplyDTO replyDTO, User user);

    @SuppressWarnings("rawtypes")
    Reply updateReply(Long id, ReplyDTO replyDTO);

    void deleteReply(Long id);

    Reply save(@SuppressWarnings("rawtypes") ReplyDTO replyDTO);

    List<Reply> getRepliesByUserId(Long userId);

    List<Reply> getRepliesByTagId(Long tagId);

    List<Reply> getRepliesByPostId(Long postId);

    List<Reply> getRepliesByTagName(String tagName);

    List<Reply> getRepliesByCommentId(Long id);
}
