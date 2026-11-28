package zin.rashidi.boot.web.apiversioning.audit;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("catalog_audit_entries")
record AuditEntry(
        @Id Long id,
        String sku,
        String action,
        String detail,
        Instant createdAt
) {
}
