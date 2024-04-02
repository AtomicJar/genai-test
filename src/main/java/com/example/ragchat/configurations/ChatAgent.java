package com.example.ragchat.configurations;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;

public interface ChatAgent {

    @SystemMessage("""
                You are a helpful assistant.
                Your task is to answer questions by providing clear and concise answers.
                
                Follow these instructions:
                - Your answer should be clear and concise, maximum 3-4 sentences
                - If you do not know the answer, you can say so
                - Use the information provided to answer, do not make up information
                - Important: Do not mention that you have been provided with additional information or documents
                """)
    TokenStream chat(String userMessage);
}