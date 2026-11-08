package zin.rashidi.boot.modulith.externalization.order;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
public record OrderDto(
        Long id,
        String customerEmail,
        BigDecimal totalAmount,
        String status
) {
}