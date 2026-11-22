package zin.rashidi.boot.security.onetimetoken.delivery;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

interface TokenDeliveryRepository extends ListCrudRepository<TokenDelivery, Long> {

    List<TokenDelivery> findByUsernameOrderByDeliveredAtDesc(String username);

    Optional<TokenDelivery> findFirstByUsernameOrderByDeliveredAtDesc(String username);

}
