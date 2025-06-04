package de.stella.agora_web.favorite.service;

import java.util.List;

import de.stella.agora_web.favorite.controller.dto.PostFavoriteDTO;

public interface IPostFavoriteService {

    List<PostFavoriteDTO> getAllFavorites();

    PostFavoriteDTO createFavorite(PostFavoriteDTO favoriteDTO);

    PostFavoriteDTO getFavorite(Long id);

    PostFavoriteDTO updateFavorite(Long id, PostFavoriteDTO favoriteDTO);

    void deleteFavorite(Long id);
}
