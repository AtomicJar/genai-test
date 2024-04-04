package com.example.ragchat.configurations;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Profile("ollama")
@Configuration
@EnableConfigurationProperties(OllamaConfiguration.OllamaProperties.class)
@RequiredArgsConstructor
@Slf4j
public class OllamaConfiguration {

    final OllamaProperties properties;

    @Bean
    @Qualifier("ollama")
    public StreamingChatLanguageModel ollamaStreamingChatLanguageModel() {
        return OllamaStreamingChatModel.builder()
                .baseUrl(properties.baseUrl())
                .seed(42)
                .temperature(0.0)
                .topP(0.0)
                .modelName(properties.model())
                .timeout(Duration.ofMinutes(2))
                .build();
    }

    @Bean
    @Qualifier("ollama")
    public ChatLanguageModel ollamaChatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl(properties.baseUrl())
                .seed(42)
                .temperature(0.0)
                .topP(0.0)
                .modelName(properties.model())
                .timeout(Duration.ofMinutes(2))
                .build();
    }

    @Validated
    @ConfigurationProperties(prefix = "ollama")
    public record OllamaProperties(String baseUrl, String model) {}
}
