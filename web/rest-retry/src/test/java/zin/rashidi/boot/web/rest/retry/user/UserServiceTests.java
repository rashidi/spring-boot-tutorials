package zin.rashidi.boot.web.rest.retry.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserService.class)
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private MockRestServiceServer server;

    @Test
    @DisplayName("Given the API returns 500 twice When getting users Then it should retry and succeed on the third attempt")
    void findAllWithFailures() throws Exception {
        var response = """
                       [{
                         "id": 1,
                         "name": "Leanne Graham",
                         "username": "Bret",
                         "email": "Sincere@april.biz"
                       }]
                       """;

        server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
              .andRespond(withServerError());

        server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
              .andRespond(withServerError());

        server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
              .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        List<User> users = this.userService.findAll();

        assertThat(users).hasSize(1)
                .first()
                .extracting("name").isEqualTo("Leanne Graham");

        server.verify();
    }

}
