package zin.rashidi.boot.web.apiversioning.catalog;

import java.math.BigDecimal;

public record ProductV1Response(
        String sku,
        String name,
        BigDecimal price,
        String currency
) {
}
