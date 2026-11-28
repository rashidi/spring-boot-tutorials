package zin.rashidi.boot.web.apiversioning.inventory;

import java.util.Collection;
import java.util.Map;

public interface InventoryCatalog {

    StockSnapshot findBySku(String sku);

    Map<String, StockSnapshot> findBySkus(Collection<String> skus);

    StockSnapshot registerInitialStock(String sku, int initialQuantity, int reorderThreshold);

}
