package de.stella.agora_web.favorite.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.favorite.controller.dto.FavoriteDTO;
import de.stella.agora_web.favorite.model.Favorite;
import de.stella.agora_web.favorite.repository.FavoriteRepository;
import de.stella.agora_web.favorite.service.IFavoriteService;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;

@Service
public class FavoriteServiceImpl implements IFavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<FavoriteDTO> getAllFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        List<FavoriteDTO> favoriteDTOs = new ArrayList<>();
        for (Favorite favorite : favorites) {
            Profile profile = favorite.getProfile();
            Post post = favorite.getPost();
            if (profile != null && post != null) {
                ProfileDTO profileDTO = ProfileDTO.builder().id(profile.getId()).build();
                PostDTO postDTO = PostDTO.builder().id(post.getId()).build();
                favoriteDTOs.add(FavoriteDTO.builder().id(favorite.getId()).profile(profileDTO).post(postDTO).build());
            }
        }
        return favoriteDTOs;
    }

    @Override
    public FavoriteDTO getFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow();

        Profile profile = favorite.getProfile();
        ProfileDTO profileDTO = profile != null ? ProfileDTO.builder().id(profile.getId()).build() : null;

        Post post = favorite.getPost();
        PostDTO postDTO = post != null ? PostDTO.builder().id(post.getId()).build() : null;

        return FavoriteDTO.builder().id(favoriteId).profile(profileDTO).post(postDTO).build();
    }

    private ProfileDTO profileFrom(Profile profile) {
        return profile != null ? ProfileDTO.builder().id(profile.getId()).build() : null;
    }

    private PostDTO postFrom(Post post) {
        return post != null ? PostDTO.builder().id(post.getId()).build() : null;
    }

    @Override
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    @Override
    public FavoriteDTO createFavorite(FavoriteDTO favoriteDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFavorite'");
    }

    @Override
    public FavoriteDTO updateFavorite(Long id, FavoriteDTO favoriteDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFavorite'");
    }

}
