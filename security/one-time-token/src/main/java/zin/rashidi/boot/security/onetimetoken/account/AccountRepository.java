package zin.rashidi.boot.security.onetimetoken.account;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

interface AccountRepository extends ListCrudRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

}
