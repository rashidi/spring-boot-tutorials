package zin.rashidi.boot.web.apiversioning.audit;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import zin.rashidi.boot.web.apiversioning.catalog.ProductCreatedEvent;

import java.time.Instant;

@Component
class CatalogAuditListener {

    private final AuditEntryRepository repository;

    CatalogAuditListener(AuditEntryRepository repository) {
        this.repository = repository;
    }

    @EventListener
    void on(ProductCreatedEvent event) {
        var detail = "Registered %s in %s".formatted(event.name(), event.category());
        repository.save(new AuditEntry(
                null,
                event.sku(),
                "PRODUCT_CREATED",
                detail,
                Instant.now()
        ));
    }

}
