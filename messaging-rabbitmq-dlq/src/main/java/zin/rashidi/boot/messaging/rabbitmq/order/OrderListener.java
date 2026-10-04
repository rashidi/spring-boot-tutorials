package zin.rashidi.boot.messaging.rabbitmq.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static zin.rashidi.boot.messaging.rabbitmq.order.OrderQueueConfiguration.ORDER_QUEUE;

/**
 * @author Rashidi Zin
 */
@Component
class OrderListener {

    private final List<Order> processedOrders = Collections.synchronizedList(new ArrayList<>());
    private final Map<Long, Integer> attempts = new ConcurrentHashMap<>();

    @RabbitListener(queues = ORDER_QUEUE)
    void process(Order order) {
        attempts.merge(order.id(), 1, Integer::sum);

        if (order.quantity() < 1) {
            throw new IllegalArgumentException("Order %d has an invalid quantity of %d".formatted(order.id(), order.quantity()));
        }

        processedOrders.add(order);
    }

    List<Order> getProcessedOrders() {
        return List.copyOf(processedOrders);
    }

    int getAttempts(Long orderId) {
        return attempts.getOrDefault(orderId, 0);
    }

}
