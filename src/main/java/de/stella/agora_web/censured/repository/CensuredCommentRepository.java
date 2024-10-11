package de.stella.agora_web.censured.repository;

import de.stella.agora_web.censured.model.CensuredComment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CensuredCommentRepository {

  public List<CensuredComment> findAll() {
    return new ArrayList<>();
  }

  public Optional<CensuredComment> findById(Long id) {
    return findAll().stream().filter(c -> c.getId().equals(id)).findFirst();
  }

  public CensuredComment save(CensuredComment censuredComment) {
    List<CensuredComment> censoredComments = findAll();
    censoredComments.add(censuredComment);
    return censuredComment;
  }

  public void deleteById(Long id) {
    List<CensuredComment> censoredComments = findAll();
    censoredComments.removeIf(c -> c.getId().equals(id));
  }

  public List<CensuredComment> findByUserId(Long userId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
