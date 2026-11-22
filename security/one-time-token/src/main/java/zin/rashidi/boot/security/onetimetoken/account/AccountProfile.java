package zin.rashidi.boot.security.onetimetoken.account;

public record AccountProfile(
        Long id,
        String username,
        String email,
        String displayName,
        String role
) {
}
