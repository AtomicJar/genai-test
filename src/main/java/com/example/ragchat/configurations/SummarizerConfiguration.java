package com.example.ragchat.configurations;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SummarizerConfiguration {

    public final static String FILE_NAME_KEY = "file_name";

    @Bean
    public DocumentTransformer summarizerDocumentTransformer(SummarizerAgent summarizerAgent) {
        return document -> {
            log.info("Summarizing document: {}", document.metadata("file_name"));
            String summary = summarizerAgent.summarize(document.text());
            log.info("Summary: {}", summary);
            return new Document(summary, Metadata.metadata("file_name", document.metadata("file_name")));
        };
    }
}
