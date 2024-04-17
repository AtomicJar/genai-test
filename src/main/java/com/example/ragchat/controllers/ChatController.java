package com.example.ragchat.controllers;

import com.example.ragchat.configurations.ChatAgent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Slf4j
@AllArgsConstructor
public class ChatController {

    final ChatAgent straight;

    final ChatAgent ragged;

    @GetMapping("/straight")
    ChatResponse straight(@RequestParam String question) {
        log.info("Straight agent");
        return new ChatResponse(straight.chat(question));
    }

    @GetMapping("/rag")
    ChatResponse rag(@RequestParam String question) {
        log.info("RAG response");
        return new ChatResponse(ragged.chat(question));
    }


    public record ChatResponse(String message) {}
}
