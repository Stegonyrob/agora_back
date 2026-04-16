package de.stella.agora_web.posts.controller.dto;

public interface PostSummaryDTO {

    Long getId();

    String getTitle();

    String getMessage();

    Integer getCommentsCount();

    Integer getFavoritesCount();

    Long getUserId();
}
