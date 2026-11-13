package zin.rashidi.boot.data.jdbc.pessimisticlocking.wallet;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
public record WalletDto(
        Long id,
        String owner,
        BigDecimal balance
) {
}