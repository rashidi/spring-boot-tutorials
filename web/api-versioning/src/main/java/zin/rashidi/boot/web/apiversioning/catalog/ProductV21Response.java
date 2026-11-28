package zin.rashidi.boot.web.apiversioning.catalog;

import zin.rashidi.boot.web.apiversioning.inventory.StockAvailability;

public record ProductV21Response(
        String sku,
        String name,
        String category,
        Money price,
        int stockQuantity,
        StockAvailability availability,
        boolean restockRecommended
) {
}
