package zin.rashidi.boot.web.apiversioning.audit;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

interface AuditEntryRepository extends ListCrudRepository<AuditEntry, Long> {

    List<AuditEntry> findBySkuOrderByCreatedAtAsc(String sku);

}
