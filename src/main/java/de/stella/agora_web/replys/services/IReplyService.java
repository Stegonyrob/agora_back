package de.stella.agora_web.replys.services;

import java.util.List;

import de.stella.agora_web.replys.controller.dto.ReplyDTO;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;


public interface IReplyService {

    List<Reply> getAllReplys();
    Reply getReplyById(Long id);
    Reply createReply(@SuppressWarnings("rawtypes") ReplyDTO replyDTO, User user);
    @SuppressWarnings("rawtypes")
    Reply updateReply(Long id, ReplyDTO replyDTO);
    void deleteReply(Long id);
   
}