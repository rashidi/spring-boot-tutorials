package zin.rashidi.boot.data.rest.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.resttestclient.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

import zin.rashidi.boot.data.rest.TestDataRestValidationApplication;

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
                .contentType(APPLICATION_JSON)
                .body(body)
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(RepositoryRestErrorResponse.class);

        assertThat(response.getResponseBody().getErrors())
                .hasSize(1)
                .extracting(ValidationError::getMessage)
                .containsExactly("Author is inactive");
    }

    private URI authorUri() {
        var body = """
                {
                  "name": "Rudyard Kipling",
                  "status": "INACTIVE"
                }
                """;

        return URI.create(restClient.post().uri("/authors")
                .contentType(APPLICATION_JSON)
                .body(body)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation().toString());
    }

    static class ValidationError {

        private String entity;
        private String property;
        private Object invalidValue;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public void setInvalidValue(Object invalidValue) {
            this.invalidValue = invalidValue;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    static class RepositoryRestErrorResponse {

        private List<ValidationError> errors = new ArrayList<>();

        public List<ValidationError> getErrors() {
            return errors;
        }

        public void setErrors(List<ValidationError> errors) {
            this.errors = errors;
        }

    }

}
