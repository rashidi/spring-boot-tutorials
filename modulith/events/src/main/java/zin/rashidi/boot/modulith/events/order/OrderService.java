package zin.rashidi.boot.modulith.events.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.boot.modulith.events.common.OrderPlacedEvent;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Rashidi Zin
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final ApplicationEventPublisher events;

    public OrderService(OrderRepository repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    @Transactional
    public Order placeOrder(String customerEmail, BigDecimal totalAmount) {
        var order = repository.save(new Order(customerEmail, totalAmount, Order.OrderStatus.CREATED));

        events.publishEvent(new OrderPlacedEvent(
                order.getId(),
                order.getCustomerEmail(),
                order.getTotalAmount(),
                Instant.now()
        ));

        return order;
    }

}