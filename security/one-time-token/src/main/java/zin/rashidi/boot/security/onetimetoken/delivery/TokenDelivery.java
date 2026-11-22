package zin.rashidi.boot.security.onetimetoken.delivery;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.net.URI;
import java.time.Instant;

@Table("token_deliveries")
record TokenDelivery(
        @Id Long id,
        String username,
        String recipientEmail,
        String tokenValue,
        String magicLink,
        Instant expiresAt,
        Instant deliveredAt
) {

    TokenDeliveryRecord toRecord() {
        return new TokenDeliveryRecord(
                id,
                username,
                recipientEmail,
                tokenValue,
                URI.create(magicLink),
                expiresAt,
                deliveredAt
        );
    }

}
