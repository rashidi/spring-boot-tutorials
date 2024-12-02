package zin.rashidi.boot.web.restclient.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Rashidi Zin
 */
@Repository
class UserRestRepository implements UserRepository {

    private final RestClient restClient;

    UserRestRepository(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com/users")
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public List<User> findAll() {
        return restClient.get()
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public User findById(Long id) {
        return restClient.get().uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new UserNotFoundException();
                }))
                .body(User.class);
    }

    static class UserNotFoundException extends RuntimeException {}

}
