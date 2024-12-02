package zin.rashidi.boot.web.restclient.post;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Rashidi Zin
 */
@Configuration
class PostRepositoryConfiguration {

    @Bean
    public PostRepository postRepository(RestClient.Builder restClientBuilder) {
        var restClient = restClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {

                    if (response.getStatusCode() == NOT_FOUND) { throw new PostNotFoundException(); }

                })
                .build();

        var factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();

        return factory.createClient(PostRepository.class);
    }

    static class PostNotFoundException extends RuntimeException {}

}
