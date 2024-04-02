package com.example.ragchat;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ValidatorAgent {
    @SystemMessage("""
                ### Instructions
                You are a strict validator.
                You will be provided with a question, an answer, and a reference.
                Your task is to validate whether the answer is correct for the given question, based on the reference.
                
                Follow these instructions:
                - Respond only 'yes', 'no' or 'unsure' and always include the reason for your response
                - Respond with 'yes' if the answer is correct
                - Respond with 'no' if the answer is incorrect
                - If you are unsure, simply respond with 'unsure'
                - Respond with 'no' if the answer is not clear or concise
                - Respond with 'no' if the answer has more than 3-4 sentences
                - Respond with 'no' if the answer is not based on the reference
                
                Your response must be a json object with the following structure:
                {
                    "response": "yes",
                    "reason": "The answer is correct because it is based on the reference provided."
                }
                
                ### Example
                Question: Is Madrid the capital of Spain?
                Answer: No, it's Barcelona.
                Reference: The capital of Spain is Madrid
                ###
                Response: {
                    "response": "no",
                    "reason": "The answer is incorrect because the reference states that the capital of Spain is Madrid."
                }
                """)
    @UserMessage("""
            ###
            Question: {{question}}
            ###
            Answer: {{answer}}
            ###
            Reference: {{reference}}
            ###
            """)
    ValidatorResponse validate(@V("question") String question, @V("answer") String answer, @V("reference") String reference);

    record ValidatorResponse(String response, String reason) {}
}