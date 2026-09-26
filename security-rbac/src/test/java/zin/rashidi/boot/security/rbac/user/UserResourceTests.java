package zin.rashidi.boot.security.rbac.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.security.rbac.TestcontainersConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
class UserResourceTests {

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(new UserEntity("admin", passwordEncoder.encode("password"), Role.ADMIN));
        userRepository.save(new UserEntity("user", passwordEncoder.encode("password"), Role.USER));
    }

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