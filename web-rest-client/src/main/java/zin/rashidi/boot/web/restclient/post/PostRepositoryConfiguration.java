package zin.rashidi.boot.web.restclient.post;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.client.support.RestClientAdapter.create;
import static org.springframework.web.service.invoker.HttpServiceProxyFactory.builderFor;

/**
 * @author Rashidi Zin
 */
@Configuration
class PostRepositoryConfiguration {

    @Bean
    public PostRepository postRepository(RestClient.Builder restClientBuilder) {
        var restClient = restClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, new PostErrorResponseHandler())
                .build();

        return builderFor(create(restClient))
                .build()
                .createClient(PostRepository.class);
    }

    static class PostErrorResponseHandler implements ErrorHandler {

        @Override
        public void handle(HttpRequest request, ClientHttpResponse response) throws IOException {

            if (response.getStatusCode() == NOT_FOUND) { throw new PostNotFoundException(); }

        }

        static class PostNotFoundException extends RuntimeException {}

    }
}
