package zin.rashidi.boot.test.contract.consumer.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sh.stubborn.contract.stubrunner.StubsMode;
import sh.stubborn.contract.stubrunner.spring.AutoConfigureStubRunner;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
        ids = "zin.rashidi.boot:test-contract-producer:+:stubs:8080",
        stubsMode = StubsMode.CLASSPATH
)
class ProductRepositoryTests {

    @Autowired
    private ProductRepository products;

    @Test
    @DisplayName("Given a product exists When I search for a product Then it should return the matching product")
    void shouldReturnProduct() {
        Product product = products.findById(1L);

        assertThat(product)
                .extracting("id", "name", "price")
                .containsOnly(1L, "MacBook Pro", 1999.99);
    }
}
