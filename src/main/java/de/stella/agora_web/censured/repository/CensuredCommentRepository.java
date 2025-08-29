package de.stella.agora_web.censured.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.censured.model.CensuredComment;

@Repository
public interface CensuredCommentRepository extends JpaRepository<CensuredComment, Long> {

    List<CensuredComment> findByUserId(Long userId);
}
