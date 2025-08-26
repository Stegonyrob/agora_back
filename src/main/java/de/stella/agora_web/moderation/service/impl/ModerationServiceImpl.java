package de.stella.agora_web.moderation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.repository.CensuredCommentRepository;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.moderation.service.IModerationService;
import de.stella.agora_web.moderation.service.ISentimentAnalysisService;

@Service
public class ModerationServiceImpl implements IModerationService {

    @Autowired
    private CensuredCommentRepository censuredCommentRepository;

    @Autowired
    private ISentimentAnalysisService sentimentAnalysisService;

    @Override
    public CensuredComment moderateComment(Comment comment) {
        String message = comment.getMessage().toLowerCase();

        // Verificar análisis de sentimientos
        String sentiment = sentimentAnalysisService.analyzeComment(comment.getMessage());

        // Verificar palabras ofensivas específicas
        if (isOffensive(sentiment) || containsOffensiveWords(message)) {
            CensuredComment censuredComment = new CensuredComment(comment.getId(), comment.getUser(),
                    "Comentario ofensivo detectado");
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
