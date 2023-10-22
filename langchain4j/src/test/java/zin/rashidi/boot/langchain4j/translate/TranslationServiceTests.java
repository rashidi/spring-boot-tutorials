package zin.rashidi.boot.langchain4j.translate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest
class TranslationServiceTests {

    @Container
    private static final ElasticsearchContainer elastic = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
    );

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elastic::getHttpHostAddress);
    }

    @BeforeAll
    static void createIndex(@Autowired ElasticsearchTemplate template) {
        template.indexOps(IndexCoordinates.of("translation")).create();
    }

    @Autowired
    private TranslationService service;

    @Test
    @DisplayName("When I request to translate the phrase Hola, ¿cómo estás? from Spanish to English Then the translation should consists of source and target language, translated text, and word breakdowns")
    void translate() {
        var result = service.translate("spanish", "english", "Hola, ¿cómo estás?");

        assertThat(result).satisfies(translate -> {

            assertThat(translate)
                    .extracting("language.source", "language.target")
                    .containsExactly("es", "en");

            assertThat(translate)
                    .extracting("text.source", "text.target")
                    .containsExactly("Hola, ¿cómo estás?", "Hello, how are you?");

            assertThat(translate)
                    .extracting("text.breakdowns").asList()
                    .extracting("source", "target")
                    .containsExactly(
                            tuple("Hola", "Hello"),
                            tuple("¿cómo estás?", "how are you?")
                    );
        });

    }

}