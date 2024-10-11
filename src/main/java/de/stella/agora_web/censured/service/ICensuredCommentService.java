package de.stella.agora_web.censured.service;

import de.stella.agora_web.censured.model.CensuredComment;
import java.util.List;

public interface ICensuredCommentService {
  List<CensuredComment> findAll();
  CensuredComment findById(Long id);
  CensuredComment save(CensuredComment censuredComment);
  void delete(Long id);
}
