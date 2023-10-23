package zin.rashidi.boot.langchain4j.translate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest
class TranslationServiceTests {

    @Container
    private static final ElasticsearchContainer elastic = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.10.2")
    )
            .withEnv("xpack.security.enabled", "false");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.elasticsearch.uri", elastic::getHttpHostAddress);
    }

    @BeforeAll
    static void createIndex() throws IOException {
        try (var client = RestClient.builder(HttpHost.create(elastic.getHttpHostAddress())).build()) {
            client.performRequest(new Request("PUT", "/translation"));
        }
    }

    @Autowired
    private TranslationService service;

    @Test
    @DisplayName("When I request to translate a phrase from Spanish to English Then the translation should consists of source and target language, translated text, and word breakdowns")
    void translate() {
        var result = service.translate("spanish", "english", "Yo soy un salsero");

        assertThat(result).satisfies(translate -> {

            assertThat(translate)
                    .extracting("language.source", "language.target", "text.source", "text.target")
                    .containsExactly("es", "en", "Yo soy un salsero", "I am a salsa dancer");

            assertThat(translate)
                    .extracting("text.breakdowns").asList()
                    .extracting("source", "target")
                    .containsExactly(
                            tuple("Yo", "I"),
                            tuple("soy", "am"),
                            tuple("un", "a"),
                            tuple("salsero", "salsa dancer")
                    );
        });

    }

}
