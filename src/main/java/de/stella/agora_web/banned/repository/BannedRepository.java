package de.stella.agora_web.banned.repository;

import de.stella.agora_web.banned.model.Banned;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {
  // Métodos para interactuar con la base de datos
  Banned findByUserId(Long userId);
  List<Banned> findByBanDateAfter(Date date);
  // ...
  int countByUserId(Long userId);
  boolean existsByUserId(Long userId);
}
