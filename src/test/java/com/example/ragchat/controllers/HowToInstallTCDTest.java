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
            - Answer must indicate to download Testcontainers Desktop from https://testcontainers.com/desktop/
            - Answer must indicate to use brew to install Testcontainers Desktop in MacOS
            - Answer must be less than 5 sentences
            """;

    @Test
    void verifyStraightAgentFailsToAnswerHowToInstallTCD() {
        String answer  = restTemplate.getForObject("/chat/straight?question={question}", ChatController.ChatResponse.class, question).message();
        //assertThat(response).doesNotContain("https://testcontainers.com/desktop/");
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        log.info("Question: {} - Answer: {}", question, answer);
        log.info("Validation: {}", validate);
        assertThat(validate.response()).isEqualTo("no");
        //double cosineSimilarity = getCosineSimilarity(reference, answer);
        //assertThat(cosineSimilarity).isLessThan(0.8);
    }

    @Test
    void verifyRaggedAgentSucceedToAnswerHowToInstallTCD() {
        String answer  = restTemplate.getForObject("/chat/rag?question={question}", ChatController.ChatResponse.class, question).message();
        //assertThat(response).contains("https://testcontainers.com/desktop/");
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        log.info("Question: {} - Answer: {}", question, answer);
        log.info("Validation: {}", validate);
        assertThat(validate.response()).isEqualTo("yes");
        //double cosineSimilarity = getCosineSimilarity(reference, answer);
        //assertThat(cosineSimilarity).isGreaterThan(0.8);
    }
}