package zin.rashidi.boot.web.virtualthreads.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import zin.rashidi.boot.web.virtualthreads.TestcontainersConfiguration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@AutoConfigureTestRestTemplate
@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", webEnvironment = RANDOM_PORT)
class UserControllerTests {

    @Autowired
    private TestRestTemplate restClient;

    @Autowired
    private UserRepository em;

    @Test
    @DisplayName("Should process web request and database query on a Virtual Thread")
    void virtualThreadEnabled() {
        em.save(new User("Rashidi"));

        var response = restClient.getForEntity("/thread-info", Map.class);

        assertThat(response)
                .extracting("statusCode", "body.isVirtual", "body.userCount")
                .containsOnly(OK, true, 1);
    }

}
