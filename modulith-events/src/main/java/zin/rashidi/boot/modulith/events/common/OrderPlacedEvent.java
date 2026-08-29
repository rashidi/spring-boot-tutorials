package zin.rashidi.boot.modulith.events.common;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Rashidi Zin
 */
public record OrderPlacedEvent(
        Long orderId,
        String customerEmail,
        BigDecimal totalAmount,
        Instant timestamp
) {
}