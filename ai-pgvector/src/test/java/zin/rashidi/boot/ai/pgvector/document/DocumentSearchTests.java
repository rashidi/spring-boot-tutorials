package zin.rashidi.boot.ai.pgvector.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import zin.rashidi.boot.ai.pgvector.TestcontainersConfiguration;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DocumentSearchTests {

    @Autowired
    private DocumentSearchService documents;

    @BeforeEach
    void add() {
        documents.add(List.of(
                new DocumentItem(
                        UUID.randomUUID(),
                        "Spring Boot simplifies microservice development with convention over configuration.",
                        Map.of("category", "framework")
                ),
                new DocumentItem(
                        UUID.randomUUID(),
                        "Testcontainers provides disposable, real database containers for integration tests.",
                        Map.of("category", "testing")
                )
        ));
    }

    @Test
    @DisplayName("should index documents and retrieve semantically similar content")
    void searchReturnsSemanticallyRelevantDocuments() {
        var results = documents.search("microservices in Java", 1, 0.0);

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting("text").isEqualTo("Spring Boot simplifies microservice development with convention over configuration.");
    }

    @TestConfiguration
    static class TestEmbeddingConfiguration {

        @Bean
        @Primary
        EmbeddingModel testEmbeddingModel() {
            return new EmbeddingModel() {

                private float[] createVector(String text) {
                    var vector = new float[384];
                    var lower = (text != null) ? text.toLowerCase() : "";

                    // Microservices / Spring cluster (doc1 and query) -> dimension 0
                    if (lower.contains("microservice") || lower.contains("spring")) {
                        vector[0] = 1.0f;
                    }

                    // Testing / Testcontainers cluster (doc2) -> dimension 1
                    else if (lower.contains("testcontainer") || lower.contains("testing")) {
                        vector[1] = 1.0f;
                    }

                    // Default / fallback -> dimension 2
                    else {
                        vector[2] = 1.0f;
                    }

                    return vector;
                }

                @Override
                public EmbeddingResponse call(EmbeddingRequest request) {
                    return request.getInstructions().stream()
                            .map(text -> new Embedding(createVector(text), 0))
                            .collect(collectingAndThen(toUnmodifiableList(), EmbeddingResponse::new));
                }

                @Override
                public float[] embed(Document document) {
                    return createVector(document.getText());
                }

            };
        }
    }

}
