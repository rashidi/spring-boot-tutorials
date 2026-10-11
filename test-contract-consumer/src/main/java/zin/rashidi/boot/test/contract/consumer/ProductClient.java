package zin.rashidi.boot.test.contract.consumer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Product getProduct(Long id) {
        return restTemplate.getForObject("http://localhost:8080/products/{id}", Product.class, id);
    }
}
