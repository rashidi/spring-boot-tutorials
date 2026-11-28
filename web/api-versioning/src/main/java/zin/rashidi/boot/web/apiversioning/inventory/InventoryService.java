package zin.rashidi.boot.web.apiversioning.inventory;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class InventoryService implements InventoryCatalog {

    private final StockItemRepository repository;

    InventoryService(StockItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public StockSnapshot findBySku(String sku) {
        return repository.findBySku(sku)
                .map(this::toSnapshot)
                .orElseGet(() -> StockSnapshot.unavailable(sku));
    }

    @Override
    public Map<String, StockSnapshot> findBySkus(Collection<String> skus) {
        if (skus.isEmpty()) {
            return Map.of();
        }
        return repository.findBySkuIn(skus).stream()
                .map(this::toSnapshot)
                .collect(Collectors.toMap(StockSnapshot::sku, Function.identity()));
    }

    @Override
    public StockSnapshot registerInitialStock(String sku, int initialQuantity, int reorderThreshold) {
        var saved = repository.save(new StockItem(null, sku, initialQuantity, reorderThreshold));
        return toSnapshot(saved);
    }

    private StockSnapshot toSnapshot(StockItem item) {
        StockAvailability availability;
        if (item.quantity() == 0) {
            availability = StockAvailability.OUT_OF_STOCK;
        } else if (item.quantity() <= item.reorderThreshold()) {
            availability = StockAvailability.LOW_STOCK;
        } else {
            availability = StockAvailability.IN_STOCK;
        }
        return new StockSnapshot(
                item.sku(),
                item.quantity(),
                availability,
                availability != StockAvailability.IN_STOCK
        );
    }

}
