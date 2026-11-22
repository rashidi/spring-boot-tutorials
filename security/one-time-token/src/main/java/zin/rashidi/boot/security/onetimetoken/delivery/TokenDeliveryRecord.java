package zin.rashidi.boot.security.onetimetoken.delivery;

import java.net.URI;
import java.time.Instant;

public record TokenDeliveryRecord(
        Long id,
        String username,
        String recipientEmail,
        String tokenValue,
        URI magicLink,
        Instant expiresAt,
        Instant deliveredAt
) {
}
