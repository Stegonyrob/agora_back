package de.stella.agora_web.posts.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.tags.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String location;
    private int loves;
    private List<Comment> comments;
    private boolean isArchived;
    private List<String> tags;
    private List<String> images;
    private boolean isPublished;
    private String altImage;
    private String sourceImage;
    private String altAvatar;
    private String sourceAvatar;
    private String userName;
    private String role;
    private String urlAvatar;

    @SuppressWarnings("rawtypes")
    private List<ReplyDTO> replies;

    public PostDTO(Post post, int lovesCount) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.message = post.getMessage();
        this.location = post.getLocation();
        this.isArchived = post.isArchived();
        this.loves = lovesCount;
        this.userId = post.getUser() != null ? post.getUser().getId() : null;
        this.comments = post.getComments();
        this.tags = post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        this.userName = post.getUser() != null ? post.getUser().getUsername() : null;
        this.role = (post.getUser() != null && post.getUser().getRoles() != null)
                ? (String) post.getUser().getRoles().stream()
                        .map(Role::getName)
                        .findFirst()
                        .orElse(null)
                : null;
    }

    public PostDTO(Boolean archived) {
        this.isArchived = archived;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String[] getTags() {
        if (tags == null) {
            return new String[0];
        }
        return tags.toArray(String[]::new);
    }

    public boolean isArchived() {
        return isArchived;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    @java.lang.SuppressWarnings(value = "all")
    @lombok.Generated
    public static class PostDTOBuilder {

        public PostDTOBuilder commentsCount(int commentsCount) {
            if (commentsCount < 0) {
                throw new IllegalArgumentException("commentsCount must not be negative");
            }
            return this;
        }

        public PostDTOBuilder favoritesCount(int favoritesCount) {
            if (favoritesCount < 0) {
                throw new IllegalArgumentException("favoritesCount must not be negative");
            }
            this.loves = favoritesCount;
            return this;
        }
    }
}
