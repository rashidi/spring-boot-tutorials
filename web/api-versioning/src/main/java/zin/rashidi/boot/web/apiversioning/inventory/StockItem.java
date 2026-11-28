package zin.rashidi.boot.web.apiversioning.inventory;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("inventory_items")
record StockItem(
        @Id Long id,
        String sku,
        int quantity,
        int reorderThreshold
) {
}
