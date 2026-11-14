package zin.rashidi.boot.data.jdbc.pessimisticlocking.wallet;

import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Override
    @Lock(LockMode.PESSIMISTIC_WRITE)
    Optional<Wallet> findById(Long id);

}