package zin.rashidi.boot.architecture.hexagonal.user.adapter.in.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.architecture.hexagonal.TestcontainersConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true"
})
class UserResourceTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("Should create and retrieve a user via HTTP")
    void createAndRetrieveUser() {
        // Create user
        restClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "name": "Rashidi"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.name").isEqualTo("Rashidi");

        // Retrieve users
        restClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Rashidi");
    }

}
