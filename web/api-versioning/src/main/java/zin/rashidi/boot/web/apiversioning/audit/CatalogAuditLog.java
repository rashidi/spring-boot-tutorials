package zin.rashidi.boot.web.apiversioning.audit;

import java.util.List;

public interface CatalogAuditLog {

    List<AuditEntryRecord> findBySku(String sku);

}
