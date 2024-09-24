package de.stella.agora_web.censured.service.impl;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.repository.CensuredCommentRepository;
import de.stella.agora_web.censured.service.ICensuredCommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CensuredCommentServiceImpl implements ICensuredCommentService {

  @Autowired
  private CensuredCommentRepository censuredCommentRepository;

  @Override
  public List<CensuredComment> findAll() {
    return censuredCommentRepository.findAll();
  }

  @Override
  public CensuredComment findById(Long id) {
    return censuredCommentRepository.findById(id).orElse(null);
  }

  @Override
  public CensuredComment save(CensuredComment censuredComment) {
    return censuredCommentRepository.save(censuredComment);
  }

  @Override
  public void delete(Long id) {
    censuredCommentRepository.deleteById(id);
  }

  public List<CensuredComment> findByUserId(Long userId) {
    return censuredCommentRepository.findByUserId(userId);
  }
}
