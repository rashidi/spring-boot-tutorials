package zin.rashidi.boot.testcontainers.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GreetingService {

    private final RestClient restClient;

    public GreetingService(@Value("${server.url:http://localhost:8080}") String serverUrl, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(serverUrl).build();
    }

    public Greeting fetchGreeting() {
        return restClient.get()
                .uri("/api/greetings")
                .retrieve()
                .body(Greeting.class);
    }
}
