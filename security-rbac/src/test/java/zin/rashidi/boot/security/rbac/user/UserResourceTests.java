package zin.rashidi.boot.security.rbac.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.security.rbac.TestcontainersConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
@Sql(executionPhase = BEFORE_TEST_CLASS, statements = {
        "INSERT INTO users (username, password, role) VALUES ('admin', '$2a$10$7vM/7c3oV1dJ5Y.q8uBWeO.L9Q2b6M.fLz6G8c.B7wZ.b4c6gB6W.', 'ADMIN')",
        "INSERT INTO users (username, password, role) VALUES ('user', '$2a$10$7vM/7c3oV1dJ5Y.q8uBWeO.L9Q2b6M.fLz6G8c.B7wZ.b4c6gB6W.', 'USER')"
})
class UserResourceTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("When unauthenticated request, then 401 Unauthorized should be returned")
    void unauthenticated() {
        restClient.get().uri("/users")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("When user without ADMIN role requests, then 403 Forbidden should be returned")
    void userRole() {
        restClient.get().uri("/users")
                .headers(headers -> headers.setBasicAuth("user", "password"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("When user with ADMIN role requests, then 200 OK should be returned")
    void adminRole() {
        restClient.get().uri("/users")
                .headers(headers -> headers.setBasicAuth("admin", "password"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2);
    }

}