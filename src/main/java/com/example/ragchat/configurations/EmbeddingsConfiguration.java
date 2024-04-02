package com.example.ragchat.configurations;

import com.example.ragchat.retrievers.CustomRetriever;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Configuration
@EnableConfigurationProperties(EmbeddingsConfiguration.EmbeddingsProperties.class)
@AllArgsConstructor
@Slf4j
public class EmbeddingsConfiguration {

    JdbcConnectionDetails connectionDetails;

    DocumentTransformer summarizerDocumentTransformer;

    EmbeddingsProperties properties;

    @Bean
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2QuantizedEmbeddingModel();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel) {
        String[] parts = connectionDetails.getJdbcUrl().split("/");
        String host = parts[2].split(":")[0];
        int port = Integer.parseInt(parts[2].split(":")[1]);
        String database = parts[3];

        EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                .host(host)
                .port(port)
                .database(database)
                .user(connectionDetails.getUsername())
                .password(connectionDetails.getPassword())
                .table("embeddings")
                .dimension(384)
                .build();

        ingest(embeddingStore, embeddingModel);
        return embeddingStore;
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return CustomRetriever.builder()
                .knowledgeBaseRoot(properties.knowledge())
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(1)
                .minScore(0.7)
                .build();
    }

    @SneakyThrows
    private void ingest(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        log.info("Ingesting embeddings into the store. This may take a while.");
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentTransformer(summarizerDocumentTransformer)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        String knowledgeBaseRoot = properties.knowledge();
        for (String file : loadFiles(Path.of(knowledgeBaseRoot), 0x42)) {
            Resource resource = new FileSystemResource(file);
            Document document = loadDocument(resource.getFile().toPath(), new TextDocumentParser());
            ingestor.ingest(document);
        }
        log.info("Ingested embeddings into the store.");
    }

    @SneakyThrows
    public static Set<String> loadFiles(Path dir, int depth) {
        try (Stream<Path> stream = Files.walk(dir, depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    @Validated
    @ConfigurationProperties(prefix = "embeddings")
    public record EmbeddingsProperties(String knowledge) {}
}
