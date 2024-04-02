package com.example.ragchat.controllers;

import com.example.ragchat.configurations.ChatAgent;
import dev.langchain4j.service.TokenStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@Slf4j
@AllArgsConstructor
public class ChatController {

    final ChatAgent straight;

    final ChatAgent ragged;

    @GetMapping("/straight")
    Flux<ChatResponse> straight(@RequestParam String question) {
        log.info("Straight agent");
        return getChatResponseFlux(question, straight);
    }

    @GetMapping("/rag")
    Flux<ChatResponse> rag(@RequestParam String question) {
        log.info("RAG response");
        return getChatResponseFlux(question, ragged);
    }

    Flux<ChatResponse> getChatResponseFlux(String message, ChatAgent chatAgent) {
        TokenStream tokenStream = chatAgent.chat(message);
        return Flux.create(sink -> tokenStream.onNext(s -> sink.next(new ChatResponse(s))).onComplete(answer -> {
            sink.complete();
        }).onError(sink::error).start());
    }

    public record ChatResponse(String message) {}
}
