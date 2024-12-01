package zin.rashidi.boot.web.restclient.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;
import zin.rashidi.boot.web.restclient.user.UserRestRepository.UserNotFoundException;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Rashidi Zin
 */
@RestClientTest(UserRestRepository.class)
class UserRepositoryTests {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("When findAll Then all users should be returned")
    void findAll() throws JsonProcessingException {
        var response = mapper.writeValueAsString(List.of(
                new User(84L, "Rashidi Zin", "rashidi.zin", "rashidi@zin.my", URI.create("rashidi.zin.my")),
                new User(87L, "Zaid Zin", "zaid.zin", "zaid@zin.my", URI.create("zaid.zin.my"))
        ));

        mockServer.expect(requestTo("https://jsonplaceholder.typicode.com/users")).andRespond(withSuccess(response, APPLICATION_JSON));

        assertThat(repository.findAll()).hasSize(2);

        mockServer.verify();
    }

    @Test
    @DisplayName("When an id is provided Then user with the id should be returned")
    void findById() throws JsonProcessingException {
        var response = mapper.writeValueAsString(
                new User(84L, "Rashidi Zin", "rashidi.zin", "rashidi@zin.my", URI.create("rashidi.zin.my"))
        );

        mockServer.expect(requestTo("https://jsonplaceholder.typicode.com/users/84")).andRespond(withSuccess(response, APPLICATION_JSON));

        assertThat(repository.findById(84L)).extracting("username").isEqualTo("rashidi.zin");

        mockServer.verify();
    }

    @Test
    @DisplayName("When an invalid user id is provided Then UserNotFoundException will be thrown")
    void findByInvalidId() {
        mockServer.expect(requestTo("https://jsonplaceholder.typicode.com/users/84")).andRespond(withBadRequest());

        assertThatThrownBy(() -> repository.findById(84L)).isInstanceOf(UserNotFoundException.class);

        mockServer.verify();
    }

}