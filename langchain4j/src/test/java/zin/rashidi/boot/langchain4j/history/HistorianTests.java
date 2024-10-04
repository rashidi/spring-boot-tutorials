package zin.rashidi.boot.langchain4j.history;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
class HistorianTests {

    @Container
    @ServiceConnection
    private static final ElasticsearchContainer elastic = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.15.2")
    )
            .withEnv("xpack.security.enabled", "false");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.elasticsearch.uri", elastic::getHttpHostAddress);
    }

    @BeforeAll
    static void createIndex() throws IOException {
        try (var client = RestClient.builder(HttpHost.create(elastic.getHttpHostAddress())).build()) {
            client.performRequest(new Request("PUT", "/history"));
        }
    }

    @Autowired
    private Historian historian;

    @Test
    @DisplayName("When I ask the Historian about the history of Malaysia in 1957, Then I should get information about Hari Merdeka")
    void chat() {
        var message = historian.chat("Malaysia", 1957);

        assertThat(message)
                .extracting("country", "year", "person")
                .containsExactly("Malaysia", 1957, "Tunku Abdul Rahman");

        assertThat(message)
                .extracting("event").asString()
                .contains("Hari Merdeka");
    }

    @Test
    @DisplayName("When I ask the Historian about event after 2021, Then an error message should be returned")
    void unsupportedYear() {
        var message = historian.chat("Malaysia", 2022);

        assertThat(message)
                .extracting("country", "year", "error")
                .containsExactly("Malaysia", 2022, "Year must be less than 2021");

        assertThat(message)
                .extracting("person", "event").asString()
                .containsWhitespaces();
    }

}
