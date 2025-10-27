package de.stella.agora_web.facades;

import org.springframework.stereotype.Component;

import de.stella.agora_web.avatar.exception.AvatarNotFoundException;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.avatar.service.IAvatarService;
import de.stella.agora_web.events.exceptions.EventNotFoundException;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.image.service.IEventImageService;
import de.stella.agora_web.image.service.IPostImageService;
import de.stella.agora_web.posts.repository.PostRepository;

@Component
public class ImageDeleteFacade {

    private final PostRepository postRepository;
    private final EventRepository eventRepository;
    private final AvatarRepository avatarRepository;
    private final IPostImageService postImageService;
    private final IEventImageService eventImageService;
    private final IAvatarService avatarService;

    public ImageDeleteFacade(PostRepository postRepository, EventRepository eventRepository,
            AvatarRepository avatarRepository, IPostImageService postImageService, IEventImageService eventImageService,
            IAvatarService avatarService) {
        this.postRepository = postRepository;
        this.eventRepository = eventRepository;
        this.avatarRepository = avatarRepository;
        this.postImageService = postImageService;
        this.eventImageService = eventImageService;
        this.avatarService = avatarService;
    }

    public String deleteImagesByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new EventNotFoundException("Post not found");
        }
        postImageService.deleteImagesByPostId(postId);
        return "ok";
    }

    public String deleteImagesByEventId(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventImageService.deleteImagesByEventId(eventId);
        return "ok";
    }

    public String deleteAvatarById(Long avatarId) {
        avatarRepository.findById(avatarId).orElseThrow(() -> new AvatarNotFoundException("Avatar not found"));
        avatarService.deleteAvatar(avatarId);
        return "ok";
    }
}
