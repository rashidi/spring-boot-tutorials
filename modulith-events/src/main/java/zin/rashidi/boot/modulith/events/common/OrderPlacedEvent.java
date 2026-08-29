package zin.rashidi.boot.modulith.events.common;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Rashidi Zin
 */
public record OrderPlacedEvent(
        UUID orderId,
        String customerEmail,
        BigDecimal totalAmount,
        Instant timestamp
) {
}