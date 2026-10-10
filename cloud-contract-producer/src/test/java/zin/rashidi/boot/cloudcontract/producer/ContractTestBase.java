package zin.rashidi.boot.cloudcontract.producer;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@SpringBootTest
@Testcontainers
public abstract class ContractTestBase {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private BookController bookController;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(bookController);

        bookRepository.deleteAll();
        bookRepository.save(new Book(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Spring Boot in Action", "Craig Walls"));
    }
}
