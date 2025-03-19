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
import lombok.Builder;

@Builder
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
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found with id: " + favoriteId));

        Profile profile = favorite.getProfile();
        ProfileDTO profileDTO = profile != null ? ProfileDTO.builder().id(profile.getId()).build() : null;

        Post post = favorite.getPost();
        PostDTO postDTO = post != null ? PostDTO.builder().id(post.getId()).build() : null;

        return FavoriteDTO.builder().id(favoriteId).profile(profileDTO).post(postDTO).build();
    }

    @Override
    public void deleteFavorite(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null.");
        }
        favoriteRepository.deleteById(id);
    }

    @Override
    public FavoriteDTO createFavorite(FavoriteDTO favoriteDTO) {
        if (favoriteDTO == null || favoriteDTO.getProfile() == null || favoriteDTO.getPost() == null) {
            throw new IllegalArgumentException("FavoriteDTO, Profile, and Post cannot be null.");
        }

        Long profileId = favoriteDTO.getProfile().getId();
        Long postId = (long) favoriteDTO.getPost().getId();
        if (profileId == null || postId == null) {
            throw new IllegalArgumentException("Profile id and Post id cannot be null.");
        }

        Profile profile = new Profile();
        profile.setId(profileId);

        Post post = new Post();
        post.setId(postId);

        Favorite favorite = new Favorite();
        favorite.setProfile(profile);
        favorite.setPost(post);
        favoriteRepository.save(favorite);

        return FavoriteDTO.builder().id(favorite.getId()).profile(favoriteDTO.getProfile()).post(favoriteDTO.getPost())
                .build();
    }

    @Override
    public FavoriteDTO updateFavorite(Long id, FavoriteDTO favoriteDTO) {
        if (id == null || favoriteDTO == null) {
            throw new IllegalArgumentException("Id and FavoriteDTO cannot be null.");
        }

        Favorite updatedFavorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found with id: " + id));

        if (favoriteDTO.getProfile() != null && favoriteDTO.getProfile().getId() != null) {
            Profile profile = new Profile();
            profile.setId(favoriteDTO.getProfile().getId());
            updatedFavorite.setProfile(profile);
        }

        if (favoriteDTO.getPost() != null) {
            Post post = new Post();
            post.setId((long) favoriteDTO.getPost().getId());
            updatedFavorite.setPost(post);
        }

        favoriteRepository.save(updatedFavorite);

        return FavoriteDTO.builder().id(id).profile(favoriteDTO.getProfile()).post(favoriteDTO.getPost()).build();
    }
}
