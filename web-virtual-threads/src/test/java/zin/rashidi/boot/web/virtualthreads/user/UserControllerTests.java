package zin.rashidi.boot.web.virtualthreads.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;
import zin.rashidi.boot.web.virtualthreads.TestcontainersConfiguration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", webEnvironment = RANDOM_PORT)
class UserControllerTests {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @Autowired
    private UserRepository users;

    @BeforeEach
    void setup(@Autowired RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();
    }

    @Test
    @DisplayName("Should process web request and database query on a Virtual Thread")
    void virtualThreadEnabled() {
        users.save(new User("Rashidi"));

        var response = restClient.get().uri("/thread-info").retrieve().toEntity(Map.class);

        assertThat(response)
                .extracting("statusCode", "body.isVirtual", "body.userCount")
                .containsOnly(OK, true, 1);
    }

}
