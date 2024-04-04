package com.example.ragchat.configurations;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Profile("openai")
@Configuration
@EnableConfigurationProperties(OpenAIConfiguration.OpenAIProperties.class)
@RequiredArgsConstructor
@Slf4j
public class OpenAIConfiguration {

    final OpenAIProperties properties;

    @Bean
    @Qualifier("openai")
    public StreamingChatLanguageModel openAIStreamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(properties.apiKey())
                .modelName(properties.model())
                .seed(42)
                .temperature(0.0)
                .topP(0.0)
                .build();
    }

    @Bean
    @Qualifier("openai")
    public ChatLanguageModel openAIChatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(properties.apiKey())
                .modelName(properties.model())
                .seed(42)
                .temperature(0.0)
                .topP(0.0)
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
