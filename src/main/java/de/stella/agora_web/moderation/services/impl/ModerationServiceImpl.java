package de.stella.agora_web.moderation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.repository.CensuredCommentRepository;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.moderation.services.IModerationService;
import de.stella.agora_web.moderation.services.ISentimentAnalysisService;

@Service
public class ModerationServiceImpl implements IModerationService {

    @Autowired
    private CensuredCommentRepository censuredCommentRepository;

    @Autowired
    private ISentimentAnalysisService sentimentAnalysisService;

    @Override
    public CensuredComment moderateComment(Comment comment) {
        String sentiment = sentimentAnalysisService.analyzeComment(comment.getMessage());
        if (isOffensive(sentiment)) {
            CensuredComment censuredComment = new CensuredComment(comment.getId(), comment.getUser(),
                    "Comentario ofensivo");
            censuredCommentRepository.save(censuredComment);
            return censuredComment;
        }
        return null;
    }

    private boolean isOffensive(String sentiment) {
        return "offensive".equals(sentiment); // Simplificado para el ejemplo
    }
}
