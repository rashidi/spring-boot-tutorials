package zin.rashidi.boot.web.apiversioning.catalog;

public record ProductCreatedEvent(
        String sku,
        String name,
        String category
) {
}
