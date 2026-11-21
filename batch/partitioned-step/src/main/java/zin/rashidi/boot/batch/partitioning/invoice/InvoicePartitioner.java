package zin.rashidi.boot.batch.partitioning.invoice;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rashidi Zin
 */
class InvoicePartitioner implements Partitioner {

    private final JdbcClient jdbcClient;

    InvoicePartitioner(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        var bounds = jdbcClient.sql("SELECT MIN(id) AS min_id, MAX(id) AS max_id FROM invoices WHERE status = 'PENDING'")
                .query((rs, rowNum) -> new IdBounds(
                        rs.getObject("min_id", Long.class),
                        rs.getObject("max_id", Long.class)))
                .single();

        if (bounds.minId() == null || bounds.maxId() == null) {
            return Map.of();
        }

        long min = bounds.minId();
        long max = bounds.maxId();
        long targetSize = Math.max(1, (max - min) / gridSize + 1);

        Map<String, ExecutionContext> partitions = new HashMap<>();
        long start = min;
        int number = 0;

        while (start <= max) {
            long end = Math.min(start + targetSize - 1, max);
            var context = new ExecutionContext();
            context.putLong("minId", start);
            context.putLong("maxId", end);
            partitions.put("partition" + number, context);

            start += targetSize;
            number++;
        }

        return partitions;
    }

    private record IdBounds(Long minId, Long maxId) {
    }

}