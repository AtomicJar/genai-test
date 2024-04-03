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
            The response *must not* mention using getMappedPort() method to find the random port of a Testcontainer.
            Instead it *must* indicate that you can use the Testcontainers Desktop app to configure fixed port.
            This way, you can connect to the container using the fixed port, without worrying about the random port.
            """;

    @Test
    void verifyStraightAgentFailsToAnswerHowToInstallTCD() {
        verify(question, "/chat/straight", (answer) -> {
            ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
            log.info("Question: {} - Answer: {}", question, answer);
            log.info("Validation: {}", validate);
            assertThat(validate.response()).isEqualTo("no");
        });
    }

    @Test
    void verifyRaggedAgentSucceedToAnswerHowToInstallTCD() {
        verify(question, "/chat/rag", (answer) -> {
            ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
            log.info("Question: {} - Answer: {}", question, answer);
            log.info("Validation: {}", validate);
            assertThat(validate.response()).isEqualTo("yes");
        });
    }
}