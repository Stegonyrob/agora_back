package de.stella.agora_web.moderation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.moderation.services.IModerationService;

@RestController
@RequestMapping("/moderation")
public class ModerationController {

    @Autowired
    private IModerationService moderationService;

    @PostMapping("/comment")
    public CensuredComment moderateComment(@RequestBody Comment comment) {
        return moderationService.moderateComment(comment);
    }
}
