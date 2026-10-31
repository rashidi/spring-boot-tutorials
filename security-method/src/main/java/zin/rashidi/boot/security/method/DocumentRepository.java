package zin.rashidi.boot.security.method;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
