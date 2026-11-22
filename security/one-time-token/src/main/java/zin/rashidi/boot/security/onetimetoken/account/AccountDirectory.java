package zin.rashidi.boot.security.onetimetoken.account;

import java.util.Optional;

public interface AccountDirectory {

    Optional<AccountProfile> findActiveByUsername(String username);

}
