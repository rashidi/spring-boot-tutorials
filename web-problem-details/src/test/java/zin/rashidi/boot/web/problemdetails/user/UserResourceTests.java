package zin.rashidi.boot.web.problemdetails.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.web.problemdetails.TestcontainersConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", webEnvironment = RANDOM_PORT)
class UserResourceTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("Should return Problem Detail when user is not found")
    void userNotFound() {
        restClient.get().uri("/users/{id}", 999L).exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/problem+json")
                .expectBody()
                .jsonPath("$.type").isEqualTo("https://zin.rashidi.my/problems/user-not-found")
                .jsonPath("$.title").isEqualTo("User Not Found")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.detail").isEqualTo("User with id 999 not found")
                .jsonPath("$.instance").isEqualTo("/users/999");
    }
}
