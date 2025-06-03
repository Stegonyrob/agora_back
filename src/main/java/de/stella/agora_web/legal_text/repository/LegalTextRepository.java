package de.stella.agora_web.legal_text.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.legal_text.model.LegalText;

public interface LegalTextRepository extends JpaRepository<LegalText, Long> {
    Optional<LegalText> findByType(String type);
}