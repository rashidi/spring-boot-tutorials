package zin.rashidi.boot.cloudcontract.producer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
