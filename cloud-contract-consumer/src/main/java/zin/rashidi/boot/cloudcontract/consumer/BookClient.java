package zin.rashidi.boot.cloudcontract.consumer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
class BookClient {

    private final RestClient restClient;

    BookClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Book getBook(String baseUrl, UUID id) {
        return restClient.get()
                .uri(baseUrl + "/books/{id}", id)
                .retrieve()
                .body(Book.class);
    }
}
