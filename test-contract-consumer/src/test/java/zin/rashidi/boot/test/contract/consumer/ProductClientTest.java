package zin.rashidi.boot.test.contract.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sh.stubborn.contract.stubrunner.spring.AutoConfigureStubRunner;
import sh.stubborn.contract.stubrunner.StubsMode;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
        ids = "zin.rashidi.boot:test-contract-producer:+:stubs:8080",
        stubsMode = StubsMode.CLASSPATH
)
class ProductClientTest {

    @Autowired
    private ProductClient productClient;

    @Test
    void shouldReturnProduct() {
        Product product = productClient.getProduct(1L);

        assertThat(product.id()).isEqualTo(1L);
        assertThat(product.name()).isEqualTo("MacBook Pro");
        assertThat(product.price()).isEqualTo(1999.99);
    }
}
