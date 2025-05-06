package de.stella.agora_web.texts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.texts.model.TextItem;

@Repository
public interface TextItemRepository extends JpaRepository<TextItem, Long> {
}