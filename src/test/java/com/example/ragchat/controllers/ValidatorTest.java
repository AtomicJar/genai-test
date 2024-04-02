package com.example.ragchat.controllers;

import com.example.ragchat.AbstractIntegrationTest;
import com.example.ragchat.ValidatorAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ValidatorTest extends AbstractIntegrationTest {
    String question = "Does 'good' have the same meaning as 'bad'?";
    String reference = "good is the opposite of bad";

    @Test
    void verifyValidatorDetectsWrongAnswer() {
        String answer = "Yes";
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        log.info("Question: {} - Answer: {}", question, answer);
        log.info("Validation: {}", validate);
        assertThat(validate.response()).isEqualTo("no");
    }

    @Test
    void verifyValidatorDetectsGoodAnswer() {
        String answer = "No";
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        log.info("Question: {} - Answer: {}", question, answer);
        log.info("Validation: {}", validate);
        assertThat(validate.response()).isEqualTo("yes");
    }
}