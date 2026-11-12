package zin.rashidi.boot.test.contract.producer.product;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;

public class ProductContractTests {

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new ProductResource());
    }
}
