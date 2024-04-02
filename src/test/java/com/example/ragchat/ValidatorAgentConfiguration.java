package com.example.ragchat;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@TestConfiguration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(ValidatorAgentConfiguration.OpenAIProperties.class)
public class ValidatorAgentConfiguration {

    final ValidatorAgentConfiguration.OpenAIProperties properties;

    @Bean
    public ValidatorAgent validator() {
        return AiServices.builder(ValidatorAgent.class)
                .chatLanguageModel(OpenAiChatModel.builder()
                        .apiKey(properties.apiKey())
                        .modelName(OpenAiChatModelName.GPT_4)
                        .seed(0)
                        .temperature(0.00000000000001)
                        .topP(0.00000000000001)
                        .build())
                .build();
    }

    @Validated
    @ConfigurationProperties(prefix = "openai")
    public record OpenAIProperties(
            String apiKey,
            String model
    ) {
    }
}
