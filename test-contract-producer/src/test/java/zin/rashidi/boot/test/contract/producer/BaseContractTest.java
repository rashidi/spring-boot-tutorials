package zin.rashidi.boot.test.contract.producer;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;

public class BaseContractTest {

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new ProductController());
    }
}
