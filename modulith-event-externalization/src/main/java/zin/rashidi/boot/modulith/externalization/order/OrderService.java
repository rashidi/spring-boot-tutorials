package zin.rashidi.boot.modulith.externalization.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Rashidi Zin
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final ApplicationEventPublisher events;

    OrderService(OrderRepository repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    @Transactional
    public OrderDto createOrder(String customerEmail, BigDecimal totalAmount) {
        var saved = repository.save(new Order(customerEmail, totalAmount, Order.OrderStatus.CREATED));
        return new OrderDto(saved.getId(), saved.getCustomerEmail(), saved.getTotalAmount(), saved.getStatus().name());
    }

    @Transactional
    public OrderDto completeOrder(Long orderId) {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.setStatus(Order.OrderStatus.COMPLETED);
        var saved = repository.save(order);

        events.publishEvent(new OrderCompletedEvent(
                saved.getId(),
                saved.getCustomerEmail(),
                saved.getTotalAmount(),
                Instant.now()
        ));

        return new OrderDto(saved.getId(), saved.getCustomerEmail(), saved.getTotalAmount(), saved.getStatus().name());
    }

}