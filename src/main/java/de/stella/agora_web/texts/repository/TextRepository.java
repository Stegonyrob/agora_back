package de.stella.agora_web.texts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.texts.model.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

    List<Text> findByCategoryAndArchivedFalse(String category);
}
