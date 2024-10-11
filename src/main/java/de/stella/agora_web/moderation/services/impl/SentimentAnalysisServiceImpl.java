package de.stella.agora_web.moderation.services.impl;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import de.stella.agora_web.moderation.model.SentimentAnalysis;
import de.stella.agora_web.moderation.services.ISentimentAnalysisService;

@Service
public class SentimentAnalysisServiceImpl implements ISentimentAnalysisService {

    private final SentimentAnalysis sentimentAnalysis;
    private static final Logger LOGGER = Logger.getLogger(SentimentAnalysisServiceImpl.class.getName());

    public SentimentAnalysisServiceImpl() throws ModelException {
        this.sentimentAnalysis = new SentimentAnalysis();
    }

    @Override
    public String analyzeComment(String comment) {
        try {
            return sentimentAnalysis.analyzeComment(comment);
        } catch (TranslateException e) {
            LOGGER.severe("Error analyzing comment: " + e.getMessage());
            return "neutral";
        }
    }

}
