package de.stella.agora_web.replys.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.replys.controller.dto.ReplyDTO;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.replys.repository.ReplyRepository;
import de.stella.agora_web.replys.services.IReplyService;
import de.stella.agora_web.user.model.User;

@Service
public class ReplyServiceImpl implements IReplyService {

    @Autowired
    private ReplyRepository replyRepository; // Assuming you have a ReplyRepository

    @Override
    public List<Reply> getAllReplys() {
        return replyRepository.findAll();
    }

    @Override
    public Reply getReplyById(Long id) {
        return replyRepository.findById(id).orElse(null);
    }

    @Override
    public Reply createReply(@SuppressWarnings("rawtypes") ReplyDTO replyDTO, User user) {
        Reply reply = new Reply();
        // Assuming ReplyDTO has fields that can be mapped to Reply
        // and User has a field that can be set to Reply
        // Example: reply.setContent(replyDTO.getContent());
        // reply.setUser(user);
        return replyRepository.save(reply);
    }

    @Override
    public Reply updateReply(Long id, @SuppressWarnings("rawtypes") ReplyDTO replyDTO) {
        Reply reply = getReplyById(id);
        if (reply != null) {
            // Update the reply fields based on replyDTO
            // Example: reply.setContent(replyDTO.getContent());
            return replyRepository.save(reply);
        }
        return null;
    }

    @SuppressWarnings("null")
    @Override
    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }

    @Override
    public Reply save(@SuppressWarnings("rawtypes") ReplyDTO replyDTO) {
        Reply reply = new Reply();
        return replyRepository.save(reply);
    }
}