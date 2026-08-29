package zin.rashidi.boot.modulith.events.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.modulith.events.core.EventPublicationRepository;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import zin.rashidi.boot.modulith.events.ModulithEventsApplication;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest(classes = ModulithEventsApplication.class)
class OrderEventPublicationTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));

    @Autowired
    private OrderService orderService;

    @Autowired
    private EventPublicationRepository publicationRepository;

    @Test
    @DisplayName("When an order is placed Then domain event is published and completed in the outbox registry")
    void placeOrder() {
        var order = orderService.placeOrder("rashidi@zin.my", BigDecimal.valueOf(99.90));

        assertThat(order).isNotNull();
        assertThat(order.getId()).isNotNull();

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            var incompletePublications = publicationRepository.findIncompletePublications();
            assertThat(incompletePublications).isEmpty();
        });
    }

}