package zin.rashidi.boot.modulith.externalization.order;

import org.springframework.modulith.events.Externalized;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Rashidi Zin
 */
@Externalized("orders.completed::#{#this.orderId()}")
public record OrderCompletedEvent(
        Long orderId,
        String customerEmail,
        BigDecimal totalAmount,
        Instant timestamp
) {
}