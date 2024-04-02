package com.example.ragchat.retrievers;

import com.example.ragchat.configurations.SummarizerConfiguration;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import lombok.Builder;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

/**
 * A {@link ContentRetriever} based on {@link dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever}
 */
@Builder
public class CustomRetriever implements ContentRetriever {

    EmbeddingStore<TextSegment> embeddingStore;
    EmbeddingModel embeddingModel;
    Integer maxResults;
    Double minScore;
    Filter filter;
    String knowledgeBaseRoot;

    @Override
    public List<Content> retrieve(Query query) {
        Embedding embeddedQuery = embeddingModel.embed(query.text()).content();
        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddedQuery)
                .maxResults(maxResults)
                .minScore(minScore)
                .filter(filter)
                .build();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);

        return searchResult.matches().stream()
                .map(EmbeddingMatch::embedded)
                .map(Content::from)
                .map(Content::textSegment)
                .map(TextSegment::metadata)
                .map(metadata -> metadata.get(SummarizerConfiguration.FILE_NAME_KEY))
                .map(this::getContent
                ).toList();
    }
    @NotNull
    @SneakyThrows
    private Content getContent(String filename) {
        Document document = loadDocument(Path.of(knowledgeBaseRoot + "/" + filename), new TextDocumentParser());
        return new Content(document.text());
    }
}
