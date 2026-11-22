package zin.rashidi.boot.security.onetimetoken.delivery;

import java.util.List;
import java.util.Optional;

public interface TokenDeliveryLog {

    List<TokenDeliveryRecord> findByUsername(String username);

    Optional<TokenDeliveryRecord> findLatestByUsername(String username);

}
