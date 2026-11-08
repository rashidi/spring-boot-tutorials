package zin.rashidi.boot.modulith.externalization.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.events.core.EventPublicationRepository;
import zin.rashidi.boot.modulith.externalization.TestcontainersConfiguration;
import zin.rashidi.boot.modulith.externalization.shipping.ShippingTracker;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("Given an order lifecycle, When an order is completed, Then the domain event is externalized to Kafka")
class OrderExternalizationIntegrationTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShippingTracker shippingTracker;

    @Autowired
    private EventPublicationRepository publicationRepository;

    @BeforeEach
    void setUp() {
        shippingTracker.clear();
    }

    @Test
    @DisplayName("Given a created order, When completeOrder is invoked, Then OrderCompletedEvent is published to the orders.completed Kafka topic")
    void orderCompletedEventIsExternalizedToKafka() {
        var created = orderService.createOrder("rashidi@zin.my", BigDecimal.valueOf(149.50));

        var completed = orderService.completeOrder(created.id());

        assertThat(completed.status()).isEqualTo("COMPLETED");

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(shippingTracker.getCompletedOrders())
                    .hasSize(1)
                    .first()
                    .satisfies(event -> {
                        assertThat(event.orderId()).isEqualTo(created.id());
                        assertThat(event.customerEmail()).isEqualTo("rashidi@zin.my");
                        assertThat(event.totalAmount()).isEqualByComparingTo(BigDecimal.valueOf(149.50));
                    });

            assertThat(publicationRepository.findIncompletePublications()).isEmpty();
        });
    }

}