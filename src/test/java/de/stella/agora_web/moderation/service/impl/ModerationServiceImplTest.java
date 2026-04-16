package de.stella.agora_web.moderation.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.repository.CensuredCommentRepository;
import de.stella.agora_web.moderation.model.ModeratableContent;
import de.stella.agora_web.moderation.service.ISentimentAnalysisService;
import de.stella.agora_web.user.model.User;

@ExtendWith(MockitoExtension.class)
class ModerationServiceImplTest {

    @Mock
    private CensuredCommentRepository censuredCommentRepository;

    @Mock
    private ISentimentAnalysisService sentimentAnalysisService;

    @InjectMocks
    private ModerationServiceImpl moderationService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUsername("testuser");
    }

    @Test
    void testSafePhrases_ShouldNotBeCensored() {
        // Given
        String[] safePhrases = {
            "hola",
            "buenos días",
            "gracias",
            "¿cómo estás?",
            "me gusta esta explicación",
            "excelente trabajo",
            "estoy de acuerdo"
        };

        // Mock sentiment analysis to return neutral for safe phrases
        when(sentimentAnalysisService.analyzeComment(anyString())).thenReturn("neutral");

        for (String phrase : safePhrases) {
            // When
            ModeratableContent content = createModeratableContent(phrase);
            CensuredComment result = moderationService.moderateComment(content);

            // Then
            assertNull(result, "Safe phrase should not be censored: " + phrase);
        }

        // Verify sentiment analysis was called for each phrase
        verify(sentimentAnalysisService, times(safePhrases.length)).analyzeComment(anyString());
    }

    @Test
    void testOffensivePhrases_ShouldBeCensored() {
        // Given
        String[] offensivePhrases = {
            "estúpido",
            "idiota",
            "imbécil",
            "tonto",
            "pendejo",
            "vete a la mierda"
        };

        // Mock sentiment analysis to return neutral (we're testing word detection)
        when(sentimentAnalysisService.analyzeComment(anyString())).thenReturn("neutral");
        when(censuredCommentRepository.save(any(CensuredComment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        for (String phrase : offensivePhrases) {
            // When
            ModeratableContent content = createModeratableContent(phrase);
            CensuredComment result = moderationService.moderateComment(content);

            // Then
            assertNotNull(result, "Offensive phrase should be censored: " + phrase);
        }
    }

    @Test
    void testOffensiveSentiment_ShouldBeCensored() {
        // Given
        when(sentimentAnalysisService.analyzeComment("this is a test")).thenReturn("offensive");
        when(censuredCommentRepository.save(any(CensuredComment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ModeratableContent content = createModeratableContent("this is a test");
        CensuredComment result = moderationService.moderateComment(content);

        // Then
        assertNotNull(result, "Content with offensive sentiment should be censored");
    }

    @Test
    void testEdgeCases_ContextDependent() {
        // Given - These should NOT be censored as they're legitimate expressions
        String[] edgeCases = {
            "matar el tiempo",
            "estoy muerto de cansancio",
            "esto es malo",
            "no me gusta"
        };

        when(sentimentAnalysisService.analyzeComment(anyString())).thenReturn("neutral");

        for (String phrase : edgeCases) {
            // When
            ModeratableContent content = createModeratableContent(phrase);
            CensuredComment result = moderationService.moderateComment(content);

            // Then
            assertNull(result, "Edge case should not be censored: " + phrase);
        }
    }

    private ModeratableContent createModeratableContent(String message) {
        return new ModeratableContent() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public User getUser() {
                return testUser;
            }
        };
    }
}
