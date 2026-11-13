package zin.rashidi.boot.data.jdbc.pessimisticlocking.wallet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import zin.rashidi.boot.data.jdbc.pessimisticlocking.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("Given a wallet domain module, When concurrent balance updates occur, Then pessimistic locking serializes access without lost updates")
class WalletPessimisticLockingTests {

    @Autowired
    private WalletService walletService;

    @Test
    @DisplayName("Given a wallet with initial balance of 100.00, When two concurrent threads each debit 40.00, Then both transactions succeed sequentially leaving a final balance of 20.00")
    void concurrentDebitsAreSerialized() throws Exception {
        var created = walletService.createWallet("Rashidi Zin", new BigDecimal("100.00"));
        var readyLatch = new CountDownLatch(2);
        var startLatch = new CountDownLatch(1);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var firstDebit = CompletableFuture.supplyAsync(() -> {
                readyLatch.countDown();
                awaitLatch(startLatch);
                return walletService.debit(created.id(), new BigDecimal("40.00"));
            }, executor);

            var secondDebit = CompletableFuture.supplyAsync(() -> {
                readyLatch.countDown();
                awaitLatch(startLatch);
                return walletService.debit(created.id(), new BigDecimal("40.00"));
            }, executor);

            assertThat(readyLatch.await(5, TimeUnit.SECONDS)).isTrue();
            startLatch.countDown();

            CompletableFuture.allOf(firstDebit, secondDebit).get(10, TimeUnit.SECONDS);
        }

        var updated = walletService.getWallet(created.id());
        assertThat(updated.owner()).isEqualTo("Rashidi Zin");
        assertThat(updated.balance()).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    @DisplayName("Given a wallet with balance of 50.00, When debit of 75.00 is requested, Then an IllegalStateException is thrown")
    void debitExceedingBalanceThrowsException() {
        var created = walletService.createWallet("Rashidi Zin", new BigDecimal("50.00"));

        assertThatThrownBy(() -> walletService.debit(created.id(), new BigDecimal("75.00")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Insufficient balance in wallet: " + created.id());
    }

    @Test
    @DisplayName("Given a non-existent wallet ID, When debit is invoked, Then an IllegalArgumentException is thrown")
    void debitNonExistentWalletThrowsException() {
        assertThatThrownBy(() -> walletService.debit(999L, new BigDecimal("10.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wallet not found: 999");
    }

    @Test
    @DisplayName("Given a non-existent wallet ID, When getWallet is invoked, Then an IllegalArgumentException is thrown")
    void getNonExistentWalletThrowsException() {
        assertThatThrownBy(() -> walletService.getWallet(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wallet not found: 999");
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}