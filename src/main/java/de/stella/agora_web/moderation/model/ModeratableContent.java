package de.stella.agora_web.moderation.model;

import de.stella.agora_web.user.model.User;

public interface ModeratableContent {

    String getMessage();

    User getUser();
    // Puedes agregar más métodos si la moderación los requiere
}
