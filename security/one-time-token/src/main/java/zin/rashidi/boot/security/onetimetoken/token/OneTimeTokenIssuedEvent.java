package zin.rashidi.boot.security.onetimetoken.token;

import java.net.URI;
import java.time.Instant;

public record OneTimeTokenIssuedEvent(
        String username,
        String recipientEmail,
        String tokenValue,
        URI magicLink,
        Instant expiresAt
) {
}
