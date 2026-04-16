package de.stella.agora_web.replies.model;

import de.stella.agora_web.moderation.model.ModeratableContent;
import de.stella.agora_web.user.model.User;

public class ModeratableReply implements ModeratableContent {

    private final String message;
    private final User user;

    public ModeratableReply(String message, User user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public User getUser() {
        return user;
    }
}
