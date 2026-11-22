package zin.rashidi.boot.security.onetimetoken.delivery;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import zin.rashidi.boot.security.onetimetoken.token.OneTimeTokenIssuedEvent;

import java.time.Instant;

@Component
class TokenDeliveryListener {

    private final TokenDeliveryRepository repository;

    TokenDeliveryListener(TokenDeliveryRepository repository) {
        this.repository = repository;
    }

    @EventListener
    void on(OneTimeTokenIssuedEvent event) {
        repository.save(new TokenDelivery(
                null,
                event.username(),
                event.recipientEmail(),
                event.tokenValue(),
                event.magicLink().toString(),
                event.expiresAt(),
                Instant.now()
        ));
    }

}
