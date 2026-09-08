package zin.rashidi.boot.web.virtualthreads.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import zin.rashidi.boot.web.virtualthreads.TestcontainersConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class UserControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Should process web request and database query on a Virtual Thread")
    void testVirtualThread() {
        repository.save(new User("Rashidi"));

        var response = restTemplate.getForEntity("/thread-info", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var body = response.getBody();
        assertThat(body).isNotNull();

        // Assert that the request was processed by a virtual thread
        assertThat((Boolean) body.get("isVirtual")).isTrue();

        // Assert that the virtual thread can successfully interact with the blocking database
        assertThat((Integer) body.get("userCount")).isGreaterThan(0);
    }
}
