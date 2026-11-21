package zin.rashidi.boot.batch.partitioning.invoice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.test.JobOperatorTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import zin.rashidi.boot.batch.partitioning.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@SpringBatchTest
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("Given an invoice batch job, When partitioned step processing runs, Then invoices are processed concurrently across worker partitions")
class BatchPartitionedStepApplicationTests {

    @Autowired
    private JobOperatorTestUtils jobs;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        jdbcClient.sql("DELETE FROM invoices").update();
    }

    @Test
    @DisplayName("Given 20 pending invoices, When processInvoicesJob runs with gridSize 4, Then 4 worker partitions execute and process all pending invoices")
    void processesAllPendingInvoicesAcrossPartitions() throws Exception {
        IntStream.rangeClosed(1, 20).forEach(i ->
                jdbcClient.sql("INSERT INTO invoices (customer, amount, status) VALUES (:customer, :amount, :status)")
                        .param("customer", "Customer " + i)
                        .param("amount", BigDecimal.valueOf(100L * i))
                        .param("status", "PENDING")
                        .update());

        var execution = jobs.startJob();

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        var workerExecutions = execution.getStepExecutions().stream()
                .filter(step -> step.getStepName().startsWith("workerStep:"))
                .toList();

        assertThat(workerExecutions).hasSize(4);
        assertThat(workerExecutions).allSatisfy(step ->
                assertThat(step.getStatus()).isEqualTo(BatchStatus.COMPLETED));

        var remainingPending = jdbcClient.sql("SELECT COUNT(*) FROM invoices WHERE status = 'PENDING'")
                .query(Long.class)
                .single();
        var processedCount = jdbcClient.sql("SELECT COUNT(*) FROM invoices WHERE status = 'PROCESSED'")
                .query(Long.class)
                .single();

        assertThat(remainingPending).isZero();
        assertThat(processedCount).isEqualTo(20L);
    }

    @Test
    @DisplayName("Given no pending invoices, When processInvoicesJob runs, Then the job completes with zero worker partitions")
    void completesCleanlyWhenNoPendingInvoicesExist() throws Exception {
        var execution = jobs.startJob();

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        var workerExecutions = execution.getStepExecutions().stream()
                .filter(step -> step.getStepName().startsWith("workerStep:"))
                .toList();

        assertThat(workerExecutions).isEmpty();
    }

}