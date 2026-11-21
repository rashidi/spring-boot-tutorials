package zin.rashidi.boot.batch.partitioning.invoice;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
record Invoice(Long id, String customer, BigDecimal amount, Status status) {

    enum Status {
        PENDING, PROCESSED
    }

}