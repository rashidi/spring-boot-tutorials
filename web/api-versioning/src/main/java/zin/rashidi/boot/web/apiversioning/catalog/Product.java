package zin.rashidi.boot.web.apiversioning.catalog;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("products")
record Product(
        @Id Long id,
        String sku,
        String name,
        String category,
        BigDecimal price,
        String currency
) {
}
