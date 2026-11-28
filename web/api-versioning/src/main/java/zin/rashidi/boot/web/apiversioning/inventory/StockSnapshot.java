package zin.rashidi.boot.web.apiversioning.inventory;

public record StockSnapshot(
        String sku,
        int quantity,
        StockAvailability availability,
        boolean restockRecommended
) {

    public static StockSnapshot unavailable(String sku) {
        return new StockSnapshot(sku, 0, StockAvailability.OUT_OF_STOCK, true);
    }

}
