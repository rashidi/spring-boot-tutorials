package zin.rashidi.boot.security.onetimetoken.account;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/accounts")
class AccountProfileController {

    private final AccountDirectory accountDirectory;

    AccountProfileController(AccountDirectory accountDirectory) {
        this.accountDirectory = accountDirectory;
    }

    @GetMapping("/me")
    AccountProfile currentAccount(Authentication authentication) {
        return accountDirectory.findActiveByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

}
