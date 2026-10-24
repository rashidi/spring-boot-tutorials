package zin.rashidi.boot.web.rest.retry;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Service
public class UserService {

    private final RestClient restClient;

    public UserService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @Retryable(
        retryFor = { HttpServerErrorException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public List<User> getUsers() {
        return this.restClient.get()
            .uri("/users")
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
    }
}
