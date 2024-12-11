package de.stella.agora_web.favorite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.favorite.model.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Override
    List<Favorite> findAll();

    @Override
    Optional<Favorite> findById(Long id);

    List<Favorite> findByProfileId(Long userId);

}
