package com.example.ragchat.configurations;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;

public interface SummarizerAgent {

    @SystemMessage("""
                You are an agent responsible of summarizing text.
                You will be given a text to summarize, and your task is to provide a concise and accurate summary of the text.
                The summary should cover all the key points and main ideas presented in the original text, while also condensing the information into a concise and easy-to-understand format.
                Please ensure that the summary includes relevant details and examples that support the main ideas, while avoiding any unnecessary information or repetition.
                The length of the summary should be appropriate for the length and complexity of the original text, providing a clear and accurate overview without omitting any important information.
                """)
    String summarize(String message);
}