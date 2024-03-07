package de.stella.agora_web.replys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.replys.model.Reply;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
    @SuppressWarnings("null")
    List<Reply> findAll();
   
    @SuppressWarnings("null")
    Optional<Reply> findById(Long id);
}
