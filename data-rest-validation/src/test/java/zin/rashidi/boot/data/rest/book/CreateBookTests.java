package zin.rashidi.boot.data.rest.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.data.rest.TestDataRestValidationApplication;

import java.net.URI;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author Rashidi Zin
 */
@AutoConfigureRestTestClient
@Import(TestDataRestValidationApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class CreateBookTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("When I create a Book with an inactive Author, I should get a Bad Request response")
    void inactiveAuthor() {
        var body = """
                {
                  "title": "If",
                  "author": "%s"
                }
                """.formatted(authorUri());

        var response = restClient.post().uri("/books")
                .headers(httpHeaders -> httpHeaders.addAll(headers()))
                .body(body)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors[0].message").isEqualTo("Author is inactive");
    }

    private URI authorUri() {
        var body = """
                {
                  "name": "Rudyard Kipling",
                  "status": "INACTIVE"
                }
                """;

        return restClient.post().uri("/authors")
                .headers(httpHeaders -> httpHeaders.addAll(headers()))
                .body(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation();
    }

    private HttpHeaders headers() {
        var headers = new HttpHeaders();

        headers.setContentType(APPLICATION_JSON);

        return headers;
    }

}
