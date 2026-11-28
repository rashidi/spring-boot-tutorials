package zin.rashidi.boot.web.apiversioning.catalog;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
}
