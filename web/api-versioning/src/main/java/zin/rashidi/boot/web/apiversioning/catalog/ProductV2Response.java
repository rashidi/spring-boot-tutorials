package zin.rashidi.boot.web.apiversioning.catalog;

import zin.rashidi.boot.web.apiversioning.inventory.StockAvailability;

public record ProductV2Response(
        String sku,
        String name,
        Money price,
        int stockQuantity,
        StockAvailability availability
) {
}
