package zin.rashidi.boot.data.jdbc.pessimisticlocking.wallet;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Query("SELECT id, owner, balance FROM wallets WHERE id = :id FOR UPDATE")
    Optional<Wallet> findByIdForUpdate(@Param("id") Long id);

}