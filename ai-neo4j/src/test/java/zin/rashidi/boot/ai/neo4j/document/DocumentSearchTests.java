package zin.rashidi.boot.ai.neo4j.document;

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
import zin.rashidi.boot.ai.neo4j.TestcontainersConfiguration;

import java.util.UUID;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DocumentSearchTests {

    @Autowired
    private DocumentSearchService documentSearchService;

    @Test
    @DisplayName("Should return similar documents based on the query")
    void search() {
        var firstItem = new DocumentItem(UUID.randomUUID().toString(), "Spring Boot is a Java-based framework used to create a micro Service.");
        var secondItem = new DocumentItem(UUID.randomUUID().toString(), "Spring AI is a project that provides a Spring-friendly API for artificial intelligence.");
        var thirdItem = new DocumentItem(UUID.randomUUID().toString(), "Testcontainers is an open source framework for providing throwaway, lightweight instances of databases, message brokers, web browsers, or just about anything that can run in a Docker container.");

        documentSearchService.add(firstItem);
        documentSearchService.add(secondItem);
        documentSearchService.add(thirdItem);

        var results = documentSearchService.search("What is Spring Boot?");

        assertThat(results)
                .extracting(DocumentItem::content)
                .contains(firstItem.content())
                .doesNotContain(thirdItem.content());
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

                    if (lower.contains("micro service") || lower.contains("spring boot")) {
                        vector[0] = 1.0f;
                    }
                    else if (lower.contains("artificial intelligence") || lower.contains("spring ai")) {
                        vector[1] = 1.0f;
                    }
                    else if (lower.contains("testcontainers") || lower.contains("docker")) {
                        vector[2] = 1.0f;
                    }
                    else {
                        vector[3] = 1.0f;
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
