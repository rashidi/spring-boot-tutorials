package zin.rashidi.boot.security.onetimetoken.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("accounts")
record Account(
        @Id Long id,
        String username,
        String email,
        String displayName,
        String role,
        boolean enabled
) {

    AccountProfile toProfile() {
        return new AccountProfile(id, username, email, displayName, role);
    }

}
