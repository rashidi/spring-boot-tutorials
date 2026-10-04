package zin.rashidi.boot.messaging.rabbitmq.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import zin.rashidi.boot.messaging.rabbitmq.TestcontainersConfiguration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static org.awaitility.Awaitility.await;
import static zin.rashidi.boot.messaging.rabbitmq.order.OrderQueueConfiguration.DEAD_LETTER_QUEUE;
import static zin.rashidi.boot.messaging.rabbitmq.order.OrderQueueConfiguration.ORDER_EXCHANGE;
import static zin.rashidi.boot.messaging.rabbitmq.order.OrderQueueConfiguration.ORDER_QUEUE;
import static zin.rashidi.boot.messaging.rabbitmq.order.OrderQueueConfiguration.ORDER_ROUTING_KEY;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrderDeadLetterTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderListener listener;

    @Test
    @DisplayName("When a valid order is published Then it should be processed and not be routed to the dead letter queue")
    void valid() {
        var order = new Order(1L, "Spring Boot in Action", 1);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, order);

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(listener.getProcessedOrders()).contains(order)
        );

        assertThat(listener.getAttempts(order.id())).isOne();
        assertThat(rabbitTemplate.receive(DEAD_LETTER_QUEUE, Duration.ofSeconds(1).toMillis())).isNull();
    }

    @Test
    @DisplayName("When an order has an invalid quantity Then it should be routed to the dead letter queue without being retried")
    void permanentFailure() {
        var order = new Order(2L, "Spring Boot in Action", 0);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, order);

        assertDeadLettered(order);
        assertThat(listener.getAttempts(order.id())).isOne();
    }

    @Test
    @DisplayName("When an ordered product remains unavailable after all retries Then it should be routed to the dead letter queue")
    void transientFailure() {
        var order = new Order(3L, "Spring Data in Action", 1);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, order);

        assertDeadLettered(order);
        assertThat(listener.getAttempts(order.id())).isEqualTo(3);
    }

    private void assertDeadLettered(Order order) {
        var deadLetter = rabbitTemplate.receive(DEAD_LETTER_QUEUE, Duration.ofSeconds(10).toMillis());

        assertThat(deadLetter).isNotNull();
        assertThat(rabbitTemplate.getMessageConverter().fromMessage(deadLetter)).isEqualTo(order);

        assertThat(deadLetter.getMessageProperties().getXDeathHeader())
                .singleElement(MAP)
                .containsEntry("queue", ORDER_QUEUE)
                .containsEntry("reason", "rejected")
                .containsEntry("exchange", ORDER_EXCHANGE);

        assertThat(listener.getProcessedOrders()).doesNotContain(order);
    }

}
