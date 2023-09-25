package zin.rashidi.boot.data.rest.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage.ValidationError;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import zin.rashidi.boot.data.rest.TestDataRestValidationApplication;

/**
 * @author Rashidi Zin
 */
@Import(TestDataRestValidationApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class CreateBookTests {

    @Autowired
    private TestRestTemplate restClient;

    @Test
    @DisplayName("When I create a Book with an inactive Author, I should get a Bad Request response")
    void inactiveAuthor() {
        var body = """
                {
                  "title": "If",
                  "author": "%s"
                }
                """.formatted(authorUri());

        var response = restClient.exchange("/books", POST, new HttpEntity<>(body, headers()), RepositoryRestErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        assertThat(response.getBody().getErrors())
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

        return restClient.exchange("/authors", POST, new HttpEntity<>(body, headers()), Void.class)
                .getHeaders()
                .getLocation();
    }

    private HttpHeaders headers() {
        var headers = new HttpHeaders();

        headers.setContentType(APPLICATION_JSON);

        return headers;
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
