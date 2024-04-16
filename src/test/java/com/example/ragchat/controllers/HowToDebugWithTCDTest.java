package com.example.ragchat.controllers;

import com.example.ragchat.AbstractIntegrationTest;
import com.example.ragchat.ValidatorAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HowToDebugWithTCDTest extends AbstractIntegrationTest {

    static String question = "How I can find the random port of a Testcontainer to connect to it?";
    static String reference = """
            - Answer must not mention using getMappedPort() method to find the random port of a Testcontainer
            - Answer must mention that you don't need to find the random port of a Testcontainer to connect to it
            - Answer must indicate that you can use the Testcontainers Desktop app to configure fixed port
            - Answer must be less than 5 sentences
            """;

    @Test
    void verifyStraightAgentFailsToAnswerHowToDebugWithTCD() {
        String answer  = restTemplate.getForObject("/chat/straight?question={question}", ChatController.ChatResponse.class, question).message();
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        log.info("Question: {} - Answer: {}", question, answer);
        log.info("Validation: {}", validate);
        assertThat(validate.response()).isEqualTo("no");
    }

    @Test
    void verifyRaggedAgentSucceedToAnswerHowToDebugWithTCD() {
        String answer  = restTemplate.getForObject("/chat/rag?question={question}", ChatController.ChatResponse.class, question).message();
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        log.info("Question: {} - Answer: {}", question, answer);
        log.info("Validation: {}", validate);
        assertThat(validate.response()).isEqualTo("yes");
    }
}