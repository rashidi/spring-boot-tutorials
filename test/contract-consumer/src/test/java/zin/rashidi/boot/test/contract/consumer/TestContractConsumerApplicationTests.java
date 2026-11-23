package zin.rashidi.boot.test.contract.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Given the consumer application, When started, Then a REST client is available")
class TestContractConsumerApplicationTests {

    @Autowired
    private RestClient restClient;

	@Test
    @DisplayName("Given the application context When loaded Then it should start without errors")
    void contextLoads() {
        assertThat(restClient).isNotNull();
	}

}
