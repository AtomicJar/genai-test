package com.example.ragchat;

import com.example.ragchat.controllers.ChatController;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.function.Consumer;

@SpringBootTest(
        classes = {
                RagChatApplication.class,
                ContainersConfiguration.class,
                ValidatorAgentConfiguration.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "embeddings.knowledge=src/test/resources/",
        }
)
@AutoConfigureWebTestClient(timeout = "100000")
public abstract class AbstractIntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected ValidatorAgent validatorAgent;

    @Autowired
    protected EmbeddingModel embeddingModel;

    protected double getCosineSimilarity(String text1, String text2) {
        Response<Embedding> embedding1 = embeddingModel.embed(text1);
        Response<Embedding> embedding2 = embeddingModel.embed(text2);
        return cosineSimilarity(embedding1.content().vector(), embedding2.content().vector());
    }

    public static double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    protected void verify(String question, String url, Consumer<String> consumer) {
        webTestClient
                .get()
                .uri(url + "?question=" + question)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ChatController.ChatResponse.class)
                .getResponseBody()
                .map(ChatController.ChatResponse::message)
                .reduce((s1, s2) -> s1 + s2)
                .as(StepVerifier::create)
                .assertNext(consumer)
                .verifyComplete();
    }
}
