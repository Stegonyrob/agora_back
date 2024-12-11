package de.stella.agora_web.favorite.service;

import java.util.List;

import de.stella.agora_web.favorite.controller.dto.FavoriteDTO;

public interface IFavoriteService {

    List<FavoriteDTO> getAllFavorites();

    FavoriteDTO createFavorite(FavoriteDTO favoriteDTO);

    FavoriteDTO getFavorite(Long id);

    FavoriteDTO updateFavorite(Long id, FavoriteDTO favoriteDTO);

    void deleteFavorite(Long id);
}