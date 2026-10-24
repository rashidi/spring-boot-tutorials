package zin.rashidi.boot.web.rest.retry.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@EnableResilientMethods
class UserService {

    private final RestClient restClient;

    public UserService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @Retryable(includes = HttpServerErrorException.class)
    public List<User> findAll() {
        return this.restClient.get()
            .uri("/users")
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
    }

}
