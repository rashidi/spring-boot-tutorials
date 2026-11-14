package zin.rashidi.boot.data.jdbc.pessimisticlocking.wallet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
@Service
public class WalletService {

    private final WalletRepository repository;

    WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public WalletDto createWallet(String owner, BigDecimal initialBalance) {
        var saved = repository.save(new Wallet(owner, initialBalance));
        return new WalletDto(saved.getId(), saved.getOwner(), saved.getBalance());
    }

    @Transactional(readOnly = true)
    public WalletDto getWallet(Long walletId) {
        var wallet = repository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found: " + walletId));
        return new WalletDto(wallet.getId(), wallet.getOwner(), wallet.getBalance());
    }

    @Transactional
    public WalletDto debit(Long walletId, BigDecimal amount) {
        var wallet = repository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found: " + walletId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance in wallet: " + walletId);
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        var saved = repository.save(wallet);
        return new WalletDto(saved.getId(), saved.getOwner(), saved.getBalance());
    }

}