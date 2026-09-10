package zin.rashidi.boot.web.virtualthreads.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.web.virtualthreads.TestcontainersConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", webEnvironment = RANDOM_PORT)
@Sql(executionPhase = BEFORE_TEST_CLASS, statements = "INSERT INTO users (name) VALUES ('Rashidi')")
class UserResourceTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("Should process web request and database query on a Virtual Thread")
    void virtualThreadEnabled() {
        restClient.get().uri("/thread-info").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.isVirtual").isEqualTo(true)
                .jsonPath("$.userCount").isEqualTo(1);
    }

}
