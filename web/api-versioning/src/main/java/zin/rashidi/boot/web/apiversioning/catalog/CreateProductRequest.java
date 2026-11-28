package zin.rashidi.boot.web.apiversioning.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotBlank String category,
        @NotNull @Positive BigDecimal price,
        @NotBlank String currency,
        @PositiveOrZero int initialQuantity,
        @PositiveOrZero int reorderThreshold
) {
}
