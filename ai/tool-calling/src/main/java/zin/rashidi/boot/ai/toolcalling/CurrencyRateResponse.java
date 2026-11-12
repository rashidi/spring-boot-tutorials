package zin.rashidi.boot.ai.toolcalling;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
public record CurrencyRateResponse(String from, String to, BigDecimal rate) {
}