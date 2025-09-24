package de.stella.agora_web.texts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.texts.model.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
}
