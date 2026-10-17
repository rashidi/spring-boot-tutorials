package zin.rashidi.boot.test.contract.consumer.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.web.client.support.RestClientAdapter.create;
import static org.springframework.web.service.invoker.HttpServiceProxyFactory.builderFor;

/**
 * @author Rashidi Zin
 */
@Configuration
class ProductRepositoryConfiguration {

    @Bean
    public ProductRepository productRepository(RestClient.Builder builder) {
        var restClient = builder
                .baseUrl("http://localhost:8080")
                .build();

        return builderFor(create(restClient))
                .build()
                .createClient(ProductRepository.class);
    }

}
