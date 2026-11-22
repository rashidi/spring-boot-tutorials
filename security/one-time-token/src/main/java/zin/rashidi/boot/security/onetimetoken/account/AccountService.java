package zin.rashidi.boot.security.onetimetoken.account;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class AccountService implements AccountDirectory, UserDetailsService {

    private final AccountRepository repository;

    AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AccountProfile> findActiveByUsername(String username) {
        return repository.findByUsername(username)
                .filter(Account::enabled)
                .map(Account::toProfile);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var account = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found: " + username));

        if (!account.enabled()) {
            throw new DisabledException("Account is disabled: " + username);
        }

        return User.withUsername(account.username())
                .password("")
                .roles(account.role())
                .build();
    }

}
