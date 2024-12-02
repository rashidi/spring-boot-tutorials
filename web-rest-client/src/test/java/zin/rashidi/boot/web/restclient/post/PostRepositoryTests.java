package zin.rashidi.boot.web.restclient.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.web.client.MockRestServiceServer;
import zin.rashidi.boot.web.restclient.post.PostRepositoryConfiguration.PostErrorResponseHandler.PostNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Rashidi Zin
 */
@RestClientTest(components = PostRepository.class, includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = PostRepositoryConfiguration.class))
class PostRepositoryTests {

    @Autowired
    private PostRepository repository;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void findAll() throws JsonProcessingException {
        var content = mapper.writeValueAsString(posts());

        mockServer.expect(requestTo("https://jsonplaceholder.typicode.com/posts")).andRespond(withSuccess(content, APPLICATION_JSON));

        repository.findAll();

        mockServer.verify();
    }

    @Test
    void findById() throws JsonProcessingException {
        var content = mapper.writeValueAsString(new Post(1L, 84L, "Spring Web: REST Clients Example with RESTClient", "An example of using RESTClient"));

        mockServer.expect(requestTo("https://jsonplaceholder.typicode.com/posts/1")).andRespond(withSuccess(content, APPLICATION_JSON));

        repository.findById(1L);

        mockServer.verify();
    }

    @Test
    void findByInvalidId() {
        mockServer.expect(requestTo("https://jsonplaceholder.typicode.com/posts/10101011")).andRespond(withResourceNotFound());

        assertThatThrownBy(() -> repository.findById(10101011L)).isInstanceOf(PostNotFoundException.class);
    }

    private List<Post> posts() {
        return List.of(
                new Post(1L, 84L, "Spring Web: REST Clients Example with RESTClient", "An example of using RESTClient"),
                new Post(2L, 84L, "Spring Web: REST Clients Example with HTTPExchange", "An example of using HttpExchange interface")
        );
    }

}