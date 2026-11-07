package zin.rashidi.boot.ai.toolcalling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
@Component
public class CurrencyRateClient {

    private final RestClient restClient;

    public CurrencyRateClient(RestClient.Builder builder, @Value("${currency.api.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public BigDecimal getRate(String from, String to) {
        var response = restClient.get()
                .uri("/rates?from={from}&to={to}", from, to)
                .retrieve()
                .body(CurrencyRateResponse.class);

        return response != null ? response.rate() : BigDecimal.ONE;
    }

}