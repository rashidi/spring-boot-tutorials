package zin.rashidi.boot.batch.partitioning.invoice;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Rashidi Zin
 */
@Configuration(proxyBeanMethods = false)
class InvoicePartitionJobConfiguration {

    @Bean
    Job processInvoicesJob(JobRepository jobRepository, Step partitionStep) {
        return new JobBuilder("processInvoicesJob", jobRepository)
                .start(partitionStep)
                .build();
    }

    @Bean
    Step partitionStep(JobRepository jobRepository, Step workerStep, InvoicePartitioner partitioner) {
        var taskExecutor = new SimpleAsyncTaskExecutor("batch-partition-");
        taskExecutor.setVirtualThreads(true);

        return new StepBuilder("partitionStep", jobRepository)
                .partitioner("workerStep", partitioner)
                .step(workerStep)
                .gridSize(4)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    InvoicePartitioner invoicePartitioner(JdbcClient jdbcClient) {
        return new InvoicePartitioner(jdbcClient);
    }

    @Bean
    Step workerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemStreamReader<Invoice> invoiceReader,
            ItemProcessor<Invoice, Invoice> invoiceProcessor,
            JdbcBatchItemWriter<Invoice> invoiceWriter) {
        return new StepBuilder("workerStep", jobRepository)
                .<Invoice, Invoice>chunk(5)
                .transactionManager(transactionManager)
                .reader(invoiceReader)
                .processor(invoiceProcessor)
                .writer(invoiceWriter)
                .build();
    }

    @Bean
    @Scope(value = "step", proxyMode = ScopedProxyMode.INTERFACES)
    ItemStreamReader<Invoice> invoiceReader(
            DataSource dataSource,
            @Value("#{stepExecutionContext['minId']}") Long minId,
            @Value("#{stepExecutionContext['maxId']}") Long maxId) {
        return new JdbcPagingItemReaderBuilder<Invoice>()
                .name("invoiceReader")
                .dataSource(dataSource)
                .selectClause("id, customer, amount, status")
                .fromClause("invoices")
                .whereClause("status = 'PENDING' AND id BETWEEN :minId AND :maxId")
                .parameterValues(Map.of("minId", minId, "maxId", maxId))
                .sortKeys(Map.of("id", Order.ASCENDING))
                .pageSize(5)
                .saveState(false)
                .rowMapper((rs, rowNum) -> new Invoice(
                        rs.getLong("id"),
                        rs.getString("customer"),
                        rs.getBigDecimal("amount"),
                        Invoice.Status.valueOf(rs.getString("status"))))
                .build();
    }

    @Bean
    ItemProcessor<Invoice, Invoice> invoiceProcessor() {
        return invoice -> new Invoice(invoice.id(), invoice.customer(), invoice.amount(), Invoice.Status.PROCESSED);
    }

    @Bean
    JdbcBatchItemWriter<Invoice> invoiceWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Invoice>()
                .dataSource(dataSource)
                .sql("UPDATE invoices SET status = :status WHERE id = :id")
                .itemSqlParameterSourceProvider(invoice -> new MapSqlParameterSource()
                        .addValue("id", invoice.id())
                        .addValue("status", invoice.status().name()))
                .build();
    }

}
