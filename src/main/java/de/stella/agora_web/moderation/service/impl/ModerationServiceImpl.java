package de.stella.agora_web.moderation.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.repository.CensuredCommentRepository;
import de.stella.agora_web.moderation.model.ModeratableContent;
import de.stella.agora_web.moderation.service.IModerationService;
import de.stella.agora_web.moderation.service.ISentimentAnalysisService;

@Service
public class ModerationServiceImpl implements IModerationService {

    private static final Logger logger = LoggerFactory.getLogger(ModerationServiceImpl.class);

    @Autowired
    private CensuredCommentRepository censuredCommentRepository;

    @Autowired
    private ISentimentAnalysisService sentimentAnalysisService;

    @Override
    public CensuredComment moderateComment(ModeratableContent content) {
        String message = content.getMessage().toLowerCase();

        // Verificar análisis de sentimientos
        String sentiment = sentimentAnalysisService.analyzeComment(content.getMessage());

        // Verificar palabras ofensivas específicas
        if (isOffensive(sentiment) || containsOffensiveWords(message)) {
            logger.info("Mensaje rechazado por contenido inapropiado: '{}' (usuario: {})", content.getMessage(), content.getUser() != null ? content.getUser().getUsername() : "anon");
            CensuredComment censuredComment = new CensuredComment(null, content.getUser(),
                    "Contenido ofensivo detectado");
            censuredCommentRepository.save(censuredComment);
            return censuredComment;
        }
        return null;
    }

    private boolean isOffensive(String sentiment) {
        return "offensive".equals(sentiment); // Simplificado para el ejemplo
    }

    private boolean containsOffensiveWords(String message) {
        // Lista de palabras ofensivas en español
        String[] offensiveWords = {
            "estúpido", "estupido", "tonto", "idiota", "imbécil", "imbecil",
            "pendejo", "gilipollas", "cabron", "cabrón", "hijo de puta",
            "jodete", "jódete", "vete a la mierda", "que te jodan",
            "maricón", "maricon", "gay" // como insulto
        };

        for (String offensiveWord : offensiveWords) {
            if (message.contains(offensiveWord)) {
                return true;
            }
        }
        return false;
    }
}
