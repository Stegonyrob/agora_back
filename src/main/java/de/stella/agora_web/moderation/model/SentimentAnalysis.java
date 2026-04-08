package de.stella.agora_web.moderation.model;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentimentAnalysis {

    private static final Logger log = LoggerFactory.getLogger(SentimentAnalysis.class);

    private static final String NEUTRAL = "neutral";
    private static final String POSITIVE = "positive";
    private static final String OFFENSIVE = "offensive";

    // Lists of offensive and positive words for basic sentiment analysis
    private static final List<String> OFFENSIVE_WORDS = Arrays.asList(
            "estúpido", "estupido", "tonto", "idiota", "imbécil", "imbecil",
            "pendejo", "gilipollas", "cabron", "cabrón", "hijo de puta",
            "jodete", "jódete", "vete a la mierda", "que te jodan",
            "maricón", "maricon", "basura", "inútil", "inutil",
            "retrasado", "mongólico", "mongolico", "autista",
            "stupid", "idiot", "moron", "loser", "worthless", "shit", "fuck"
    );

    private static final List<String> POSITIVE_WORDS = Arrays.asList(
            "gracias", "excelente", "genial", "perfecto", "bueno", "bien",
            "me gusta", "increíble", "fantástico", "maravilloso",
            "thank you", "excellent", "great", "perfect", "good", "well",
            "amazing", "fantastic", "wonderful", "love"
    );

    private static final List<String> SAFE_GREETINGS = Arrays.asList(
            "hola", "hello", "buenos días", "buenas tardes", "buenas noches",
            "good morning", "good afternoon", "good evening", "hi", "hey"
    );

    public SentimentAnalysis() {
        // Basic implementation without external model for now
        // In future, this could be enhanced with actual ML models
    }

    public String analyzeComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            return NEUTRAL;
        }

        String lowerComment = comment.toLowerCase().trim();

        // Check for safe greetings first
        for (String greeting : SAFE_GREETINGS) {
            if (lowerComment.equals(greeting) || lowerComment.startsWith(greeting + " ")) {
                return POSITIVE;
            }
        }

        // Check for offensive content
        for (String offensiveWord : OFFENSIVE_WORDS) {
            if (lowerComment.contains(offensiveWord)) {
                return OFFENSIVE;
            }
        }

        // Check for positive content
        int positiveScore = 0;
        for (String positiveWord : POSITIVE_WORDS) {
            if (lowerComment.contains(positiveWord)) {
                positiveScore++;
            }
        }

        // Simple scoring system
        if (positiveScore > 0) {
            return POSITIVE;
        }

        // Check for question patterns (usually neutral/positive intent)
        if (lowerComment.contains("?") || lowerComment.startsWith("¿")) {
            return NEUTRAL;
        }

        // Check for thank you patterns
        if (lowerComment.contains("gracias") || lowerComment.contains("thank")) {
            return POSITIVE;
        }

        // Default to neutral for unknown content
        return NEUTRAL;
    }

    /**
     * Enhanced analysis that considers context and patterns
     */
    public boolean isLikelyOffensive(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            return false;
        }

        String lowerComment = comment.toLowerCase().trim();

        // Immediate offensive word detection
        for (String offensiveWord : OFFENSIVE_WORDS) {
            if (lowerComment.contains(offensiveWord)) {
                return true;
            }
        }

        // Pattern detection for spam (excessive repetition)
        if (isSpamPattern(lowerComment)) {
            return true;
        }

        // Aggressive capitalization (all caps with length > 10)
        return comment.length() > 10 && comment.equals(comment.toUpperCase())
                && !comment.matches(".*\\d.*"); // Not if it contains numbers
    }

    private boolean isSpamPattern(String comment) {
        // Check for excessive repetition of characters
        if (comment.matches(".*([a-z])\\1{4,}.*")) {
            return true;
        }

        // Check for excessive repetition of words
        String[] words = comment.split("\\s+");
        if (words.length > 3) {
            String firstWord = words[0];
            int repetitions = 0;
            for (String word : words) {
                if (word.equals(firstWord)) {
                    repetitions++;
                }
            }
            if (repetitions > words.length / 2) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("java:S1172")
    public static void main(String[] args) {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();

        // Test cases
        String[] testCases = {
            "hola",
            "buenos días",
            "gracias por tu ayuda",
            "estúpido",
            "eres un idiota",
            "me gusta esta explicación",
            "¿puedes ayudarme?",
            "excelente trabajo"
        };

        for (String test : testCases) {
            String result = sentimentAnalysis.analyzeComment(test);
            log.info("'{}' -> {}", test, result);
        }
    }
}
