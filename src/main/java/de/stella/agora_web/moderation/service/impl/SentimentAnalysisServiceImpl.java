package de.stella.agora_web.moderation.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.stella.agora_web.moderation.model.SentimentAnalysis;
import de.stella.agora_web.moderation.service.ISentimentAnalysisService;

@Service
public class SentimentAnalysisServiceImpl implements ISentimentAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(SentimentAnalysisServiceImpl.class);

    private final SentimentAnalysis sentimentAnalysis;

    public SentimentAnalysisServiceImpl() {
        this.sentimentAnalysis = new SentimentAnalysis();
    }

    @Override
    public String analyzeComment(String comment) {
        try {
            return sentimentAnalysis.analyzeComment(comment);
        } catch (Exception e) {
            log.error("Error analyzing comment: {}", e.getMessage(), e);
            return "neutral";
        }
    }

}
