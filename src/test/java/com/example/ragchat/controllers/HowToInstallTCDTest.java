package com.example.ragchat.controllers;

import com.example.ragchat.AbstractIntegrationTest;
import com.example.ragchat.ValidatorAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HowToInstallTCDTest extends AbstractIntegrationTest {

    static String question = "How I can install Testcontainers Desktop?";
    static String reference = """                    
                    You can go to https://testcontainers.com/desktop/ and download your OS-specific client application and install it.
                    If you are using MacOS, then you can install the Testcontainers Desktop app easily using
                    *brew install atomicjar/tap/testcontainers-desktop* command.
                    ===
                    Important: the download url must be included in the answer to be considered correct.
                    """;

    @Test
    void verifyStraightAgentFailsToAnswerHowToInstallTCD() {
        verify(question, "/chat/straight", (answer) -> {
            //assertThat(response).doesNotContain("https://testcontainers.com/desktop/");
            ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
            log.info("Question: {} - Answer: {}", question, answer);
            log.info("Validation: {}", validate);
            assertThat(validate.response()).isEqualTo("no");
            //double cosineSimilarity = getCosineSimilarity(reference, answer);
            //assertThat(cosineSimilarity).isLessThan(0.8);
        });
    }

    @Test
    void verifyRaggedAgentSucceedToAnswerHowToInstallTCD() {
        verify(question, "/chat/rag", (answer) -> {
            //assertThat(response).contains("https://testcontainers.com/desktop/");
            ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
            log.info("Question: {} - Answer: {}", question, answer);
            log.info("Validation: {}", validate);
            assertThat(validate.response()).isEqualTo("yes");
            //double cosineSimilarity = getCosineSimilarity(reference, answer);
            //assertThat(cosineSimilarity).isGreaterThan(0.8);
        });
    }
}