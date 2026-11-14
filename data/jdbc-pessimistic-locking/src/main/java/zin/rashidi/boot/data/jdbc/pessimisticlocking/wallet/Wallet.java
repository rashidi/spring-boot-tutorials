package zin.rashidi.boot.data.jdbc.pessimisticlocking.wallet;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
@Table("wallets")
class Wallet {

    @Id
    private Long id;

    private final String owner;
    private BigDecimal balance;

    Wallet(String owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
    }

    Long getId() {
        return id;
    }

    String getOwner() {
        return owner;
    }

    BigDecimal getBalance() {
        return balance;
    }

    void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}