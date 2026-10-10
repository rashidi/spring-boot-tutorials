package zin.rashidi.boot.cloudcontract.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "zin.rashidi.boot:cloud-contract-producer:+:stubs:8080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ConsumerApplicationTests {

    @Autowired
    private BookClient bookClient;

    @Test
    void shouldReturnBook() {
        UUID bookId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Book book = bookClient.getBook("http://localhost:8080", bookId);

        assertThat(book).isNotNull();
        assertThat(book.id()).isEqualTo(bookId);
        assertThat(book.title()).isEqualTo("Spring Boot in Action");
        assertThat(book.author()).isEqualTo("Craig Walls");
    }
}
