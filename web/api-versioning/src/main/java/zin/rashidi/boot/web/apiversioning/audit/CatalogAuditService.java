package zin.rashidi.boot.web.apiversioning.audit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CatalogAuditService implements CatalogAuditLog {

    private final AuditEntryRepository repository;

    CatalogAuditService(AuditEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AuditEntryRecord> findBySku(String sku) {
        return repository.findBySkuOrderByCreatedAtAsc(sku).stream()
                .map(entry -> new AuditEntryRecord(
                        entry.sku(),
                        entry.action(),
                        entry.detail(),
                        entry.createdAt()
                ))
                .toList();
    }

}
