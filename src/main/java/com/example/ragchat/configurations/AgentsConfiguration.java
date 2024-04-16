package com.example.ragchat.configurations;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AgentsConfiguration {

    @Bean(name = "ragged")
    ChatAgent ragged(ChatLanguageModel chatLanguageModel,
                     ContentRetriever contentRetriever) {
        return AiServices.builder(ChatAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .contentRetriever(contentRetriever)
                .build();
    }

    @Bean(name = "straight")
    ChatAgent straight(ChatLanguageModel chatLanguageModel) {
        return AiServices.builder(ChatAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .build();
    }

    @Bean(name = "summarizer")
    SummarizerAgent summarizer(ChatLanguageModel chatLanguageModel) {
        return AiServices.builder(SummarizerAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .build();
    }
}