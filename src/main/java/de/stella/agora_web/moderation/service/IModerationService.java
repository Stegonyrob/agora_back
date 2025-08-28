package de.stella.agora_web.moderation.service;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.moderation.model.ModeratableContent;

public interface IModerationService {

    CensuredComment moderateComment(ModeratableContent content);
}
