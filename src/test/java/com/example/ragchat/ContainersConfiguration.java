package com.example.ragchat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        var image = DockerImageName.parse("pgvector/pgvector:pg16").asCompatibleSubstituteFor("postgres");
        return new PostgreSQLContainer<>(image).withLabel("com.testcontainers.desktop.service", "vector")
                .withReuse(true);
    }

    @Bean
    @ConditionalOnProperty(name = "ollama.containerized", havingValue = "true")
    public OllamaContainer ollama(DynamicPropertyRegistry registry, @Value("${ollama.model}") String model) {
        var ollamaContainer =
                new OllamaContainer(DockerImageName.parse("langchain4j/ollama-" + model + ":latest")
                        .asCompatibleSubstituteFor("ollama/ollama"))
                        .withReuse(true);
        registry.add("ollama.baseUrl", () -> "http://%s:%d".formatted(ollamaContainer.getHost(), ollamaContainer.getFirstMappedPort()));
        return ollamaContainer;
    }
}