package zin.rashidi.boot.security.onetimetoken.delivery;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class TokenDeliveryService implements TokenDeliveryLog {

    private final TokenDeliveryRepository repository;

    TokenDeliveryService(TokenDeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TokenDeliveryRecord> findByUsername(String username) {
        return repository.findByUsernameOrderByDeliveredAtDesc(username).stream()
                .map(TokenDelivery::toRecord)
                .toList();
    }

    @Override
    public Optional<TokenDeliveryRecord> findLatestByUsername(String username) {
        return repository.findFirstByUsernameOrderByDeliveredAtDesc(username)
                .map(TokenDelivery::toRecord);
    }

}
