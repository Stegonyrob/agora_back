package de.stella.agora_web.replys.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.replys.model.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAll();

    Optional<Reply> findById(Long id);
}
