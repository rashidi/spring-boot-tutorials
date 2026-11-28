package zin.rashidi.boot.web.apiversioning.audit;

import java.time.Instant;

public record AuditEntryRecord(
        String sku,
        String action,
        String detail,
        Instant createdAt
) {
}
