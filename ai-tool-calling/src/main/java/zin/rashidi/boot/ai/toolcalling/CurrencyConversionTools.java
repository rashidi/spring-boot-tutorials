package zin.rashidi.boot.ai.toolcalling;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
@Component
public class CurrencyConversionTools {

    private final CurrencyRateClient rateClient;

    public CurrencyConversionTools(CurrencyRateClient rateClient) {
        this.rateClient = rateClient;
    }

    @Tool(description = "Convert an amount from one fiat currency to another using real-time exchange rates")
    public BigDecimal convertCurrency(
            @ToolParam(description = "Source 3-letter currency code, e.g. USD") String from,
            @ToolParam(description = "Target 3-letter currency code, e.g. MYR") String to,
            @ToolParam(description = "Amount to convert") BigDecimal amount) {
        var rate = rateClient.getRate(from, to);
        return amount.multiply(rate);
    }

}