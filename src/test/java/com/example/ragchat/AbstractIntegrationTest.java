package com.example.ragchat;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

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
    protected TestRestTemplate restTemplate;

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
}
